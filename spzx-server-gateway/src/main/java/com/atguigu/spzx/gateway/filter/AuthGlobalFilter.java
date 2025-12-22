package com.atguigu.spzx.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered { //实现全局过滤器和优先级
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求路径
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        //判单请求路径
        if (antPathMatcher.match("/api/**/auth/**", path)) {
            //登录校验
            UserInfo userInfo = this.getUserInfo(request);
            if (userInfo == null) {
                //返回错误信息
                return out(exchange.getResponse(), ResultCodeEnum.LOGIN_AUTH);
            }
        }
        return chain.filter(exchange);//放行
    }

    private UserInfo getUserInfo(ServerHttpRequest request) {
        String token = "";
        List<String> tokenList = request.getHeaders().get("token");
        if (tokenList != null) {
            token = tokenList.get(0);
        }
        if (StrUtil.isNotBlank(token)) {
            //根据token查询redis
            String userinfoJson = redisTemplate.opsForValue().get(token);
            if (StrUtil.isNotBlank(userinfoJson)) {
                UserInfo userInfo = JSON.parseObject(userinfoJson, UserInfo.class);
                return userInfo;
            } else {
                return null;
            }
        }
        return null;
    }

    //返回错误信息
    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        Result result = Result.build(null, resultCodeEnum);
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
