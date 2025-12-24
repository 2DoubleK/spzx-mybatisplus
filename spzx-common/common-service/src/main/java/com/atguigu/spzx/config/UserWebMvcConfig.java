package com.atguigu.spzx.config;

import com.atguigu.spzx.interceptor.UserLoginAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component //哪里需要用就注入
public class UserWebMvcConfig implements WebMvcConfigurer {
    //1.引入拦截器
    @Autowired
    private UserLoginAuthInterceptor loginAuthInterceptor;

    //2.注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginAuthInterceptor)
                .addPathPatterns("/api/**");  //拦截路径
    }
}
