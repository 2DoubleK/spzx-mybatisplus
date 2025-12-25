package com.atguigu.spzx.order.controller;

import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.h5.OrderSubmitVo;
import com.atguigu.spzx.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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


    //  auth/buy/3,直接购买,不仅过购物车直接返回tradeVo
    @GetMapping("/auth/buy/{skuId}")
    @Operation(summary = "立即購買")
    public Result buy(@PathVariable Long skuId) {
        return orderService.buy(skuId);
    }

    //     获取当前用户的所有订单，/auth/1/10?orderStatus=
    @GetMapping("/auth/{page}/{limit}")
    @Operation(summary = "獲取訂單分頁列表")
    public Result queryAllOrder(@PathVariable("page") Integer page,
                                @PathVariable("limit") Integer limit,
                                @RequestParam(required = false,defaultValue = "") Integer orderStatus  //訂單狀態可以是空
    ) {
        return orderService.queryAllOrder(page, limit, orderStatus);
    }
}
