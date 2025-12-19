package com.atguigu.spzx.manager.service.impl;

import ch.qos.logback.core.util.TimeUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.alibaba.fastjson2.util.UUIDUtils;
import com.atguigu.spzx.manager.service.ValidateCodeService;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.atguigu.spzx.manager.Utils.Constant.USER_VALIDATE_CODE;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //生成图片验证码
    @Override
    public ValidateCodeVo generateValidateCode() {
        // 1.通过更具生成图片验证码，hutool的CaptureUtil
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(150, 40);
        String validateCode = captcha.getCode();
        String image = captcha.getImageBase64();  //返回图片，编码实base64
        // 2.将验证码存储到redis，并且设置过期时间，
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set(USER_VALIDATE_CODE + key, validateCode, 1, TimeUnit.MINUTES);
        // 3.返回validateVo对象
        ValidateCodeVo validateCodeVo = new ValidateCodeVo();
        validateCodeVo.setCodeKey(key);//把 UUID 传给前端方便后面获取比对
        validateCodeVo.setCodeValue("data:image/png;base64,"+image); //注意是data不是date
        return validateCodeVo;
    }
}
