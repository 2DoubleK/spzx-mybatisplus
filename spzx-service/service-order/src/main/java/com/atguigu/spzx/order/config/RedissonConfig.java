package com.atguigu.spzx.order.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private String redisPort;

//    @Value("${spring.data.redis.password:}")
//    private String redisPassword;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 单节点模式（集群/哨兵模式参考Redisson官方文档）
        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort)
//                .setPassword(redisPassword.isEmpty() ? null : redisPassword)
                .setConnectTimeout(3000) // 连接超时
                .setTimeout(3000); // 命令超时

        return Redisson.create(config);
    }
}
