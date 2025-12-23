package com.atguigu.spzx.cart.controller;

import com.atguigu.spzx.cart.service.CartService;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order/cart")
@Tag(name = "购物车接口")
public class CartController {
    @Autowired
    private CartService cartService;

    // auth/addToCart/{skuId}/{skuNum}
    @Operation(summary = "添加购物车")
    @GetMapping("/auth/addToCart/{skuId}/{skuNum}")
    public Result addToCart(@PathVariable("skuId") Long skuId,
                            @PathVariable("skuNum") Integer skuNum) {

        return cartService.putIntoCart(skuId, skuNum);
    }

    //  auth/cartList
    @Operation(summary = "查詢購物車信息")
    @GetMapping("auth/cartList")
    public Result cartList() {
        return cartService.cartList();
    }

    ///auth/checkCart/
    @Operation(summary = "选中购物车商品")
    @GetMapping("auth/checkCart/{skuId}/{isChecked}")
    public Result checkCart(@PathVariable("skuId") Long skuId,
                            @PathVariable("isChecked") Integer isChecked) {
        return cartService.checkCart(skuId,isChecked);
    }
}
