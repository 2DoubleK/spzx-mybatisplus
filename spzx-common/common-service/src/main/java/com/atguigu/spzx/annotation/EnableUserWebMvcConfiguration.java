package com.atguigu.spzx.annotation;

import com.atguigu.spzx.config.UserWebMvcConfig;
import com.atguigu.spzx.interceptor.UserLoginAuthInterceptor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//使用注解，哪里需要使用这个拦截器就在那里加
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Import(value = { UserLoginAuthInterceptor.class , UserWebMvcConfig.class}) //手动将指定的类注册到 Spring 容器中
public @interface EnableUserWebMvcConfiguration {

}