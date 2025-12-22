package com.atguigu.spzx.product.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")  //读取配置文件信息，服务器地址
    private String endpoint;

    @Value("${minio.access-key}") //账号
    private String accessKey;

    @Value("${minio.secret-key}")//密码
    private String secretKey;

    // 将MinioClient注入Spring容器
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

}
