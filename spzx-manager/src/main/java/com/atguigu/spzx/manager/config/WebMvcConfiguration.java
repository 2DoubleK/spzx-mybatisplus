package com.atguigu.spzx.manager.config;

import com.atguigu.spzx.manager.interceptor.LoginAuthInterceptor;
import com.atguigu.spzx.manager.properties.UserProperties;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMvcConfiguration implements WebMvcConfigurer {
    //1.引入拦截器
    @Autowired
    private LoginAuthInterceptor loginAuthInterceptor;
    @Autowired
    private UserProperties userProperties;

    //2.注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginAuthInterceptor)
//                .excludePathPatterns("/admin/system/index/getUserInfo",//放行路径
//                        "/admin/system/index/generateValidateCode",
//                        "/admin/system/index/logout")
                .excludePathPatterns(userProperties.getNoAuthUrls())
                .addPathPatterns("/**");  //拦截器生效路径
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*");
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}
