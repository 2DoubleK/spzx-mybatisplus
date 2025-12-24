package com.atguigu.spzx.order.controller;

import com.atguigu.spzx.model.entity.order.OrderItem;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.OrderSubmitVo;
import com.atguigu.spzx.order.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order/orderInfo")
@Tag(name = "订单接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/auth/trade")
    public Result trade() {
        return orderService.trade();
    }

    @PostMapping("/auth/submitOrder")
    public Result submitOrder(@RequestBody OrderSubmitVo orderSubmitVo) {
        OrderSubmitVo vo = orderSubmitVo;
        return orderService.submitOrder(vo.getFeightFee(),
                vo.getOrderItemList(),
                vo.getRemark(),
                vo.getUserAddressId());
    }
    //  auth/214
    @GetMapping("/auth/{orderId}")
    public Result queryOrderByRebackOrderId(@PathVariable("orderId") Long orderId) {
        return orderService.queryOrderByRebackOrderId(orderId);
    }

}
