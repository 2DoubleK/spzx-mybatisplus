package com.atguigu.spzx.cart.service.impl;

import com.alibaba.fastjson2.JSON;
import org.springframework.util.CollectionUtils;

import java.util.*;

import com.atguigu.spzx.cart.service.CartService;
import com.atguigu.spzx.model.entity.h5.CartInfo;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.utils.AuthContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.apache.dubbo.config.annotation.DubboReference;
import com.atguigu.spzx.service.client.service.ProductApiSkuService;
import org.springframework.transaction.annotation.Transactional;

import static com.atguigu.spzx.model.constants.Constants.USER_CART_KEY;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @DubboReference(check = false, protocol = "dubbo")
    private ProductApiSkuService productApiSkuService;


    @Override
    public Result putIntoCart(Long skuId, Integer skuNum) {
        // 1.获取用户登录id
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = USER_CART_KEY + userId;
        // 2.从Redis中获取当前商品的购物车信息
        Object cartObj = redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId));
        CartInfo cartInfo = null;

        if (cartObj != null) {
            // 3.商品已存在，更新数量
            cartInfo = com.alibaba.fastjson.JSON.parseObject(cartObj.toString(), CartInfo.class);
            cartInfo.setSkuNum(cartInfo.getSkuNum() + skuNum);
            cartInfo.setIsChecked(1);
            cartInfo.setUpdateTime(new Date());
        } else {
            // 4.商品不存在，新建购物车对象
            cartInfo = new CartInfo();
            try {
                ProductSku productSku = productApiSkuService.getSkuBySkuId(skuId);
                cartInfo.setUserId(userId); // 补充用户ID
                cartInfo.setCartPrice(productSku.getSalePrice());
                cartInfo.setSkuNum(skuNum);
                cartInfo.setSkuId(skuId);
                cartInfo.setImgUrl(productSku.getThumbImg());
                cartInfo.setSkuName(productSku.getSkuName());
                cartInfo.setIsChecked(1);
                cartInfo.setCreateTime(new Date());
                cartInfo.setUpdateTime(new Date());
            } catch (Exception e) {
                log.error("远程调用失败：", e);
                throw new RuntimeException(e);
            }
        }
        // 5.将更新后的购物车信息存入Redis
        redisTemplate.opsForHash().put(cartKey, String.valueOf(skuId), JSON.toJSONString(cartInfo));

        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result cartList() {
        // 1.获取用户ID
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = USER_CART_KEY + userId;

        // 2.从Redis中获取购物车所有数据
        List<Object> cartObjList = redisTemplate.opsForHash().values(cartKey);
        List<CartInfo> cartInfoList = new ArrayList<>();

        // 3.判断是否有数据，有则反序列化
        if (cartObjList != null && !cartObjList.isEmpty()) {
            for (Object obj : cartObjList) {
                // 使用fastjson反序列化（与存储时一致）
                CartInfo cartInfo = JSON.parseObject(obj.toString(), CartInfo.class);
                cartInfoList.add(cartInfo);
            }
        }

        return Result.build(cartInfoList, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result checkCart(Long skuId, Integer isChecked) {
        try {
            //1.获取用户信息
            Long userId = AuthContextUtil.getUserInfo().getId();
            String cartKey = USER_CART_KEY + userId;
            //2.从redis拿到对应的hashmap,反序列化然后重新set值后序列化
            Object cartObj = redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId));
            CartInfo cartInfo = new CartInfo();
            if (cartObj != null) {
                cartInfo = JSON.parseObject(cartObj.toString(), CartInfo.class);
            }
            cartInfo.setIsChecked(isChecked);
            String objJSON = JSON.toJSONString(cartInfo);
            redisTemplate.opsForHash().put(cartKey, String.valueOf(skuId), objJSON); //必须使String类型哦序列化是String类型
        } catch (Exception e) {
            log.error("选中失败？：{}", e);
        }
        //更新redis
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result deleteCart(Long skuId) {
        //1.获取用户信息
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = USER_CART_KEY + userId;
        //2.刪除對應skuid的商品信息
        redisTemplate.opsForHash().delete(cartKey, String.valueOf(skuId));
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result clearCart() {
        //1.获取用户信息
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = USER_CART_KEY + userId;
        //2.刪除對應skuid的商品信息
        redisTemplate.delete(cartKey);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result allCheckCart(Integer isChecked) {
        try {
            //1.获取用户信息
            Long userId = AuthContextUtil.getUserInfo().getId();
            String cartKey = USER_CART_KEY + userId;
            //2.刪除對應skuId的商品信息
            Map<Object, Object> objects = new HashMap<>();
            objects = redisTemplate.opsForHash().entries(cartKey);
            if(objects==null){
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
            objects.forEach((skuIdObj,cartJsonObj)->{
                CartInfo cartInfo=JSON.parseObject(cartJsonObj.toString(),CartInfo.class);
                cartInfo.setIsChecked(1);
                String afterUpdateCartJson = JSON.toJSONString(cartInfo);
                String skuIdJson=String.valueOf(skuIdObj);
                redisTemplate.opsForHash().put(cartKey,skuIdJson,afterUpdateCartJson);
            });
        } catch (Exception e) {
            log.error("全選失敗？：{}",e);
            throw new RuntimeException(e);
        }
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
