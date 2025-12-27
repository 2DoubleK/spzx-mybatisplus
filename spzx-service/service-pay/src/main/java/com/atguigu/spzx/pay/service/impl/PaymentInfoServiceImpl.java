package com.atguigu.spzx.pay.service.impl;

import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.pay.PaymentInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.pay.mapper.PaymentInfoMapper;
import com.atguigu.spzx.pay.service.PaymentInfoService;
import com.atguigu.spzx.service.user.client.service.OrderInfoApiService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.Date;

@Service
//@DubboService  //其实对于这种只有一个方法的我们完全可以，就让PaymentService的实现类实现api接口就行了，利用多实现
@Slf4j
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {
    @Autowired
    private PaymentInfoMapper paymentInfoMapper;
    @DubboReference
    private OrderInfoApiService orderInfoApiService;

    @Override
    public Result savePaymentInfo(String orderNo) {
        PaymentInfo paymentInfo = null;
        try {
            //1.根据订单编号查询支付记录
            LambdaQueryWrapper<PaymentInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PaymentInfo::getIsDeleted, 0).eq(PaymentInfo::getOrderNo, orderNo);
            paymentInfo = paymentInfoMapper.selectOne(wrapper);
            //2.判断支付记录是否存在
            //不存在
            if (paymentInfo == null) {
                //远程调用订单信息
                OrderInfo orderInfo = orderInfoApiService.getOrderInfoByOrderNo(orderNo);
                if (orderInfo == null) {
                    return Result.build(null, ResultCodeEnum.NO_SUCH_ORDER);
                }
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
        //存在
        return Result.build(paymentInfo, ResultCodeEnum.SUCCESS);
    }
}
