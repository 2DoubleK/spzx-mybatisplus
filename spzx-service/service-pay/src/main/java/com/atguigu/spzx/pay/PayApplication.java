package com.atguigu.spzx.pay;

import com.atguigu.spzx.annotation.EnableUserWebMvcConfiguration;
import com.atguigu.spzx.pay.utils.AlipayProperties;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = {"com.atguigu.spzx.pay.mapper"})
@EnableCaching
@ComponentScan(basePackages = {"com.atguigu.spzx"})  //Knife4jConfig即，swagger不和当前服务在一个包下，额外配置
@EnableUserWebMvcConfiguration  //开启拦截和过滤器，同时会把用户信息写入threadLocal里
@EnableConfigurationProperties(value = {AlipayProperties.class})
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }
}