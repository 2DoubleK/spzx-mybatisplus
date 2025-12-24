package com.atguigu.spzx.manager.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.utils.AuthContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import static com.atguigu.spzx.manager.Utils.Constant.USER_LOGIN_TOKEN;

@Component
public class LoginAuthInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取请求方式
        String method = request.getMethod();
        if ("OPTIONS".equals(method)) {
            return true; //放行
        }
        //2 从请求获取token
        String token = request.getHeader("token");
        //3 如果请求头的token为空，返回false，返回错误信息
        if (token == null) {
            responseNoLoginInfo(response);
            return false;
        }
        //4 根据token，在redis查询信息
        String userInfoString = redisTemplate.opsForValue().get(USER_LOGIN_TOKEN + token);

        //5 redis查不到信息，返回错误信息
        if (StrUtil.isEmpty(userInfoString)) {
            return false;
        }
        //6 查询到信息注解放行,并且将用户信息放入threadLocal
        AuthContextUtil.set(JSON.parseObject(userInfoString, SysUser.class));
        //7 更新redis里的token的时间,然后放行
        redisTemplate.expire(USER_LOGIN_TOKEN+token,30, TimeUnit.MINUTES);
        return true;
    }

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//    }

    //之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthContextUtil.remove();
    }

    //响应208状态码给前端
    private void responseNoLoginInfo(HttpServletResponse response) {
        Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }
}
