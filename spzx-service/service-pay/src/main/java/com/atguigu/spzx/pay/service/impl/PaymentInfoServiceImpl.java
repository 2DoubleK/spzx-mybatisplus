package com.atguigu.spzx.pay.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.atguigu.spzx.exception.GuiguException;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.pay.PaymentInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.pay.mapper.PaymentInfoMapper;
import com.atguigu.spzx.pay.service.PaymentInfoService;
import com.atguigu.spzx.pay.utils.AlipayProperties;
import com.atguigu.spzx.service.user.client.service.OrderInfoApiService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
//@DubboService  //其实对于这种只有一个方法的我们完全可以，就让PaymentService的实现类实现api接口就行了，利用多实现
@Slf4j
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {
    @Autowired
    private PaymentInfoMapper paymentInfoMapper;
    @DubboReference
    private OrderInfoApiService orderInfoApiService;
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private AlipayProperties alipayProperties;

    @Override
    public PaymentInfo savePaymentInfo(String orderNo) {
        //1.根据订单编号保存
        PaymentInfo paymentInfo = null;
        try {
            LambdaQueryWrapper<PaymentInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PaymentInfo::getIsDeleted, 0).eq(PaymentInfo::getOrderNo, orderNo);
            paymentInfo = paymentInfoMapper.selectOne(wrapper);
            //2.判断支付记录是否存在
            //不存在
            if (paymentInfo == null) {
                //远程调用订单信息
                OrderInfo orderInfo = orderInfoApiService.getOrderInfoByOrderNo(orderNo);
                paymentInfo = new PaymentInfo();
                paymentInfo.setUserId(orderInfo.getUserId());
                paymentInfo.setOrderNo(orderNo);
                paymentInfo.setPaymentStatus(0);
                paymentInfo.setAmount(orderInfo.getTotalAmount());
                paymentInfo.setPayType(orderInfo.getPayType());
                String content = "order";
                if (orderInfo.getOrderItemList() != null && !orderInfo.getOrderItemList().isEmpty()) {
                    content = orderInfo.getOrderItemList().get(0).getSkuName();
                }
                paymentInfo.setContent(content);
                paymentInfo.setCallbackContent(null);
                paymentInfo.setOutTradeNo(orderNo);
                paymentInfo.setUpdateTime(new Date());
                paymentInfo.setCreateTime(new Date());
                paymentInfo.setCallbackTime(null);
                //封装paymentInfo
                paymentInfoMapper.insert(paymentInfo);
            }
        } catch (Exception e) {
            log.error("订单创建失败：{}", e);
            throw new RuntimeException(e);
        }
        return paymentInfo;
    }

    @Override
    public String submitAlipay(String orderNo) {
        String body = null;
        try {
            //1.保存支付信息
            PaymentInfo paymentInfo = savePaymentInfo(orderNo);
            //2.调用支付宝 
            AlipayTradeWapPayRequest alipayTradeRequest = new AlipayTradeWapPayRequest();
            //同步回调 
            alipayTradeRequest.setReturnUrl(alipayProperties.getReturnPaymentUrl());

            //异步回调 
            alipayTradeRequest.setNotifyUrl(alipayProperties.getNotifyPaymentUrl());

            //准备请求参数 
            HashMap<String, Object> map = new HashMap<>();
            map.put("out_trade_no",paymentInfo.getOrderNo());
            map.put("product_code","QUICK_WAP_WAY");
            //map.put("total_amount",paymentInfo.getAmount());
            map.put("total_amount",new BigDecimal("0.01"));
            map.put("subject",paymentInfo.getContent());
            alipayTradeRequest.setBizContent(JSON.toJSONString(map));

            //调用支付包接口
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayTradeRequest);
            if (response.isSuccess()) {
                body = response.getBody();
                return body;
            } else {
                throw new GuiguException(ResultCodeEnum.DATA_ERROR);
            }
        } catch (AlipayApiException e) {
            log.error("支付错误,{}", e);
            throw new RuntimeException(e);
        }
    }
}
