package com.atguigu.spzx.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.model.entity.h5.CartInfo;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.order.OrderItem;
import com.atguigu.spzx.model.entity.order.OrderLog;
import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.TradeVo;
import com.atguigu.spzx.order.mapper.OrderItemMapper;
import com.atguigu.spzx.order.mapper.OrderLogMapper;
import com.atguigu.spzx.order.mapper.OrderMapper;
import com.atguigu.spzx.order.service.OrderService;
import com.atguigu.spzx.service.user.client.service.UserAddressApiService;
import com.atguigu.spzx.utils.AuthContextUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.atguigu.spzx.model.constants.Constants.USER_CART_KEY;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderInfo> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderLogMapper orderLogMapper; // 新增订单日志Mapper
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @DubboReference
    private UserAddressApiService userAddressApiService;

    // 线程安全的日期格式化器
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER = ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyyMMddHHmmss")
    );

    // 生成交易信息（返回结算页：TradeVo  OrderItem + 总金额）
    @Override
    public Result<TradeVo> trade() {
        // 1. 获取登录用户（强制判空，未登录返回鉴权失败）
        UserInfo loginUser = AuthContextUtil.getUserInfo();
        if (loginUser == null) {
            log.warn("trade方法：用户未登录");
            return Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        }
        Long userId = loginUser.getId();

        // 2. 构建购物车Redis Key
        String cartKey = USER_CART_KEY + userId;

        // 3. 从Redis获取并过滤选中的购物车商品
        List<CartInfo> checkedCartList = getCheckedCartList(cartKey);
        if (checkedCartList.isEmpty()) {
            log.error("trade方法：用户{}暂无选中的购物车商品", userId);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }

        // 4. 计算选中商品总金额（保留2位小数）
        BigDecimal totalAmount = calculateTotalAmount(checkedCartList);

        // 5. 构建订单项列表（适配OrderItem字段）
        List<OrderItem> orderItemList = buildOrderItemList(checkedCartList);

        // 6. 封装结算TradeVO
        TradeVo tradeVo = new TradeVo();
        tradeVo.setTotalAmount(totalAmount);
        tradeVo.setOrderItemList(orderItemList);
        log.info("trade方法：用户{}生成交易信息，总金额{}，商品数{}", userId, totalAmount, orderItemList.size());
        return Result.build(tradeVo, ResultCodeEnum.SUCCESS);
    }

    // 提交订单（事务：OrderInfo + OrderItem + OrderLog）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> submitOrder(Long feightFee, List<OrderItem> orderItemList, String remark, Long userAddressId) {
        // 1. 校验用户登录
        UserInfo loginUser = AuthContextUtil.getUserInfo();
        if (loginUser == null) {
            log.warn("submitOrder方法：用户未登录");
            return Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        }
        Long userId = loginUser.getId();

        // 2. 校验订单项非空
        if (orderItemList == null || orderItemList.isEmpty()) {
            log.error("submitOrder方法：用户{}订单项为空", userId);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }

        // 3. 计算订单总金额（基于订单项重新计算，避免前端篡改）
        BigDecimal feightFeeBigDecimal = new BigDecimal(feightFee);
        BigDecimal totalAmount = calculateOrderTotalAmount(orderItemList,feightFeeBigDecimal);

        // 4. 获取用户默认收货地址（必填）
        UserAddress address = null;
        try {
            address = userAddressApiService.getUserDefaultAddress(userId, userAddressId);
            if (address == null) {
                log.error("submitOrder方法：用户{}未设置默认收货地址", userId);
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        } catch (Exception e) {
            log.error("远程调用获取信息失败",e);
        }

        // 5. 构建并保存订单主表（OrderInfo）
        OrderInfo orderInfo = buildOrderBaseInfo(loginUser, totalAmount, address, remark);
        orderMapper.insert(orderInfo); // 入库订单主表
        Long orderId = orderInfo.getId();
        log.info("submitOrder方法：用户{}订单{}主表入库成功", userId, orderId);

        // 6. 构建并保存订单项（OrderItem）- 关联订单ID
        List<OrderItem> finalOrderItemList = orderItemList.stream()
                .peek(item -> item.setOrderId(orderId)) // 关联订单ID
                .collect(Collectors.toList());
        try {
            orderItemMapper.insertBatch(finalOrderItemList); // 批量插入订单项，或者使用saveBatch()
        } catch (Exception e) {
            log.info("插入orderItem失败",e);
        }
        log.info("submitOrder方法：用户{}订单{}订单项入库{}条", userId, orderId, finalOrderItemList.size());

        // 7. 构建并保存订单日志（OrderLog）
        try {
            OrderLog orderLog = buildOrderLog(orderId, loginUser, "提交订单", orderInfo.getOrderStatus());
            orderLogMapper.insert(orderLog); // 入库订单日志
            log.info("submitOrder方法：用户{}订单{}日志入库成功", userId, orderId);
        } catch (Exception e) {
            log.info("插入orderLog失败",e);
        }

        // 8. 下单成功后删除Redis购物车（仅删除选中商品，或整车，根据业务）
        String cartKey = USER_CART_KEY + userId;
        HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();
        for (OrderItem item : orderItemList) {
            ops.delete(cartKey, item.getSkuId().toString());
        }
        log.info("submitOrder方法：用户{}购物车{}已删除", userId, cartKey);

        return Result.build(orderId, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result queryOrderByRebackOrderId(Long orderId) {
        return Result.build(query().eq("id",orderId).one(),ResultCodeEnum.SUCCESS);
    }

    /**
     * 从Redis获取并过滤选中的购物车商品（isChecked=1）
     */
    private List<CartInfo> getCheckedCartList(String cartKey) {
        List<Object> cartObjList = Optional.ofNullable(redisTemplate.opsForHash().values(cartKey))
                .orElse(new ArrayList<>());

        return cartObjList.stream()
                .filter(Objects::nonNull)
                .map(obj -> JSON.parseObject(obj.toString(), CartInfo.class))
                .filter(Objects::nonNull)
                .filter(cart -> 1 == cart.getIsChecked())
                .collect(Collectors.toList());
    }

    /**
     * 计算购物车选中商品总金额
     */
    private BigDecimal calculateTotalAmount(List<CartInfo> checkedCartList) {
        return checkedCartList.stream()
                .filter(item -> item.getSkuNum() != null && item.getCartPrice() != null)
                .map(item -> item.getCartPrice().multiply(BigDecimal.valueOf(item.getSkuNum())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 提交訂單時，基于订单项计算订单总金额（防前端篡改）
     */
    private BigDecimal calculateOrderTotalAmount(List<OrderItem> orderItemList, BigDecimal feightFeeBigDecimal) {
        return orderItemList.stream()
                .filter(item -> item.getSkuNum() != null && item.getSkuPrice() != null)
                .map(item -> item.getSkuPrice().multiply(BigDecimal.valueOf(item.getSkuNum())))
                .reduce(feightFeeBigDecimal, BigDecimal::add)//從傳入的雁南飛開始加
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 构建订单项列表（CartInfo → OrderItem）
     */
    private List<OrderItem> buildOrderItemList(List<CartInfo> checkedCartList) {
        return checkedCartList.stream()
                .map(cart -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setSkuId(cart.getSkuId());
                    orderItem.setSkuName(cart.getSkuName());
                    orderItem.setThumbImg(cart.getImgUrl());
                    orderItem.setSkuPrice(cart.getCartPrice());
                    orderItem.setSkuNum(cart.getSkuNum());
                    return orderItem;
                })
                .collect(Collectors.toList());
    }

    /**
     * 构建订单基础信息（含收货地址、金额等）
     */
    private OrderInfo buildOrderBaseInfo(UserInfo loginUser, BigDecimal totalAmount, UserAddress address, String remark) {
        OrderInfo orderInfo = new OrderInfo();

        // 1. 用户信息
        orderInfo.setUserId(loginUser.getId());
        orderInfo.setNickName(loginUser.getNickName());

        // 2. 订单号（唯一）
        orderInfo.setOrderNo(generateOrderNo(loginUser.getId()));

        // 3. 金额信息
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setOriginalTotalAmount(totalAmount); // 原价=总金额（无优惠券时）
        orderInfo.setCouponAmount(BigDecimal.ZERO);    // 优惠券金额默认0
        orderInfo.setFeightFee(BigDecimal.ZERO);      // 运费默认0

        // 4. 收货地址信息
        orderInfo.setRemark(remark);
        orderInfo.setReceiverName(address.getName());
        orderInfo.setReceiverPhone(address.getPhone());
        orderInfo.setReceiverTagName(address.getTagName());
        orderInfo.setReceiverProvince(address.getProvinceCode());
        orderInfo.setReceiverCity(address.getCityCode());
        orderInfo.setReceiverDistrict(address.getDistrictCode());
        orderInfo.setReceiverAddress(address.getAddress());

        // 5. 订单状态（0-待付款）、支付方式（1-微信）
        orderInfo.setOrderStatus(0);
        orderInfo.setPayType(1);

        return orderInfo;
    }

    /**
     * 构建订单日志
     */
    private OrderLog buildOrderLog(Long orderId, UserInfo loginUser, String note, Integer orderStatus) {
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderId);
        orderLog.setProcessStatus(orderStatus); // 关联订单状态
        orderLog.setUpdateTime(new Date());  // 操作时间
        orderLog.setOperateUser(Optional.ofNullable(loginUser.getNickName()).orElse(loginUser.getId().toString())); // 操作人（优先昵称）
        orderLog.setNote(note);               // 操作备注

        return orderLog;
    }

    /**
     * 生成唯一订单号：用户ID + 时间戳 + 6位随机数
     */
    private String generateOrderNo(Long userId) {
        String timeStr = DATE_FORMATTER.get().format(new Date());
        String randomStr = String.format("%06d", ThreadLocalRandom.current().nextInt(100000, 999999));
        return userId + timeStr + randomStr;
    }
}