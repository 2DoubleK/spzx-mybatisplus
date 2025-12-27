package com.atguigu.spzx.pay.utils; // 修正包路径注释，保持实际包路径一致

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 支付宝支付配置属性类
 */
@Data
@ConfigurationProperties(prefix = "spzx.alipay")
public class AlipayProperties {

    // 支付宝网关地址
    private String alipayUrl;
    // 商户私钥
    private String appPrivateKey;
    // 支付宝公钥
    private String alipayPublicKey;
    // 应用ID
    private String appId;
    // 同步回调地址（支付成功后跳转前端页面）
    private String returnPaymentUrl;
    // 异步回调地址（支付宝主动通知后端）
    private String notifyPaymentUrl;

    // 静态常量：支付宝接口固定参数（无需配置）
    public final static String FORMAT = "json";       // 请求数据格式
    public final static String CHARSET = "utf-8";     // 字符编码
    public final static String SIGN_TYPE = "RSA2";    // 签名算法（支付宝推荐）
}