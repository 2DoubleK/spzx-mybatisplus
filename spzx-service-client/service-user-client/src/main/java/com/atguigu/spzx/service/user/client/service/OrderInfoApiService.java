package com.atguigu.spzx.service.user.client.service;

import com.atguigu.spzx.model.entity.order.OrderInfo;

public interface OrderInfoApiService {
    OrderInfo getOrderInfoByOrderNo(String orderNo);
}
