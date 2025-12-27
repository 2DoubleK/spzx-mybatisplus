package com.atguigu.spzx.order.service.impl;

import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.order.mapper.OrderMapper;
import com.atguigu.spzx.service.user.client.service.OrderInfoApiService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DubboService
public class OrderInfoApiServiceImpl implements OrderInfoApiService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderInfo getOrderInfoByOrderNo(String orderNo) {
        return orderMapper.selectOne(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getOrderNo, orderNo)
                .eq(OrderInfo::getIsDeleted, 0));
    }
}
