package com.atguigu.spzx.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan(basePackages = {"com.atguigu.spzx.cart.mapper"})
@EnableCaching
public class CartApplication {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}