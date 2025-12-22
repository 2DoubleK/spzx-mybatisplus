package com.atguigu.spzx.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.cart.service.CartService;
import com.atguigu.spzx.model.entity.h5.CartInfo;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.atguigu.spzx.model.constants.Constants.USER_CART_KEY;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate redisTemplate;

    //添加商品到购物车
    @Override
    public Result putIntoCart(Long skuId, Integer skuNum) {
        //1.获取用户登录id
        Long userId = AuthContextUtil.getUserInfo().getId();
        String key = USER_CART_KEY + userId;
        //2.使用hash结构其实就是一个key对应一个map集合
        Object cartObj = redisTemplate.opsForHash().get(key, String.valueOf(skuId));//相当于先获取map，再根据map的key取值或拿值
        //3.如果购物车已经存在商品，那就把商品数量相加
        CartInfo cartInfo = null;
        if (cartObj != null) {
            cartInfo = JSON.parseObject(cartObj.toString(), CartInfo.class);
            cartInfo.setSkuNum(cartInfo.getSkuNum() + skuNum);//设置数量相加
            cartInfo.setIsChecked(1); //选中状态
            cartInfo.setUpdateTime(new Date());
        } else {
            //4.如果购物车没有商品就把商品直接加到购物车
            cartInfo = new CartInfo();
            //5.通过nacos+openfeign，根据skuId获取商品sku信息
            ProductSku productSku = null;
            cartInfo.setCartPrice(productSku.getSalePrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setSkuId(skuId);
            cartInfo.setImgUrl(productSku.getThumbImg());
            cartInfo.setSkuName(productSku.getSkuName());
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
            redisTemplate.opsForHash().put(key, String.valueOf(skuId), JSON.toJSONString(cartInfo));
        }

        return null;
    }
}
