package com.atguigu.spzx.order.service.impl;

import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.order.OrderItem;
import com.atguigu.spzx.order.mapper.OrderItemMapper;
import com.atguigu.spzx.order.mapper.OrderMapper;
import com.atguigu.spzx.service.user.client.service.OrderInfoApiService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@DubboService
public class OrderInfoApiServiceImpl implements OrderInfoApiService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public OrderInfo getOrderInfoByOrderNo(String orderNo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo = orderMapper.selectOne(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getOrderNo, orderNo)
                .eq(OrderInfo::getIsDeleted, 0));
        List<OrderItem> orderItemslList = new ArrayList<>();
        orderItemslList = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderId, orderInfo.getId())
                .eq(OrderItem::getIsDeleted, 0));
        if (orderItemslList != null) {
            orderInfo.setOrderItemList(orderItemslList);
        }
        return orderInfo;
    }
}
