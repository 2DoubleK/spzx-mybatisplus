package com.atguigu.spzx.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.atguigu.spzx.user.mapper"})
@EnableCaching
@ComponentScan(basePackages = {"com.atguigu.spzx"})  //Knife4jConfig即，swagger不和当前服务在一个包下，额外配置
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}