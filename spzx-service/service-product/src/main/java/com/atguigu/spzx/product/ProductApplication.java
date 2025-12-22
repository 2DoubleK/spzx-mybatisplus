package com.atguigu.spzx.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@MapperScan(basePackages = {"com.atguigu.spzx.product.mapper"})
@EnableCaching
@ComponentScan(basePackages = {"com.atguigu.spzx"})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//忽略數據庫配置
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

}