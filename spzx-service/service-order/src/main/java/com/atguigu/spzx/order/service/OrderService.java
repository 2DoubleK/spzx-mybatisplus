package com.atguigu.spzx.order.service;

import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.order.OrderItem;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface OrderService extends IService<OrderInfo> {
    Result trade();

    Result submitOrder(Long feightFee, List<OrderItem> orderItemList, String remark, Long userAddressId);

    Result queryOrderByRebackOrderId(Long orderId);

    Result queryAllOrder(Integer page, Integer limit, Integer orderStatus);

    Result buy(Long skuId);
}
