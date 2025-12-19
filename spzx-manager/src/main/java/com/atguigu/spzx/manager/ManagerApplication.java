package com.atguigu.spzx.manager;

import com.atguigu.spzx.manager.properties.UserProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.atguigu.spzx"})  //Knife4jConfig即，swagger不和当前服务在一个包下，额外配置
@EnableConfigurationProperties(value = {UserProperties.class}) //开启配置文件读取
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class,args);
    }
}
