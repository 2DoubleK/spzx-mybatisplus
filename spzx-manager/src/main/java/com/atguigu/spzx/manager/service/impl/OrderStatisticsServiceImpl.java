package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.order.OrderStatisticsMapper;
import com.atguigu.spzx.manager.service.OrderStatisticsService;
import com.atguigu.spzx.model.entity.order.OrderStatistics;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderStatisticsServiceImpl extends  ServiceImpl<OrderStatisticsMapper, OrderStatistics> implements OrderStatisticsService {
}
