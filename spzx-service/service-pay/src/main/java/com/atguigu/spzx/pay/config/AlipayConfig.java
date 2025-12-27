package com.atguigu.spzx.pay.config; // 建议放到config包下，更规范

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.atguigu.spzx.pay.utils.AlipayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝客户端配置类（生成AlipayClient核心对象）
 */
@Configuration
@EnableConfigurationProperties(AlipayProperties.class) // 开启配置属性绑定
public class AlipayConfig {

    // 注入支付宝配置属性（解决空指针问题）
    @Autowired
    private AlipayProperties alipayProperties;

    /**
     * 生成支付宝核心交互对象（单例Bean）
     */
    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(
                alipayProperties.getAlipayUrl(),          // 支付宝网关
                alipayProperties.getAppId(),              // 应用ID
                alipayProperties.getAppPrivateKey(),      // 商户私钥
                AlipayProperties.FORMAT,                  // 数据格式（json）
                AlipayProperties.CHARSET,                 // 字符编码（utf-8）
                alipayProperties.getAlipayPublicKey(),    // 支付宝公钥
                AlipayProperties.SIGN_TYPE                // 签名算法（RSA2）
        );
    }
}