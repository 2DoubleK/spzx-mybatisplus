package com.atguigu.spzx.cart.service;

import com.atguigu.spzx.model.vo.common.Result;

public interface CartService {
    Result putIntoCart(Long skuId, Integer skuNum);
}
