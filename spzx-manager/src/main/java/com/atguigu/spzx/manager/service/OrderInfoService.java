package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.order.OrderStatisticsDto;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderInfoService extends IService<OrderInfo> {
    Result getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto);
}
