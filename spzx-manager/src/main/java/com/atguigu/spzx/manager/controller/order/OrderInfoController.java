package com.atguigu.spzx.manager.controller.order;

import com.atguigu.spzx.manager.service.OrderInfoService;
import com.atguigu.spzx.model.dto.order.OrderStatisticsDto;
import com.atguigu.spzx.model.vo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order/orderInfo")
public class OrderInfoController {
    @Autowired
    private OrderInfoService orderInfoService;
    @GetMapping("getOrderStatisticsData")
    public Result getOrderStatisticsData( OrderStatisticsDto orderStatisticsDto){
        return orderInfoService.getOrderStatisticsData( orderStatisticsDto);
    }
}
