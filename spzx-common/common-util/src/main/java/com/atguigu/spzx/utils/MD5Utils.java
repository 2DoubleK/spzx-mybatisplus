package com.atguigu.spzx.utils;

import org.springframework.util.DigestUtils;

public class MD5Utils {
    public static String MD5Encrypted(String content){
        return DigestUtils.md5DigestAsHex(content.getBytes());
    }
}
