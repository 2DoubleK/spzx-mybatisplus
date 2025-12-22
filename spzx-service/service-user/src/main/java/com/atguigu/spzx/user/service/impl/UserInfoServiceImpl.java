package com.atguigu.spzx.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.common.exception.GuiguException;
import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.UserInfoVo;
import com.atguigu.spzx.user.mapper.UserInfoMapper;
import com.atguigu.spzx.user.service.UserInfoService;
import com.atguigu.spzx.utils.AuthContextUtil;
import com.atguigu.spzx.utils.HttpUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.atguigu.spzx.model.constants.Constants.USER_INFO_KEY;
import static com.atguigu.spzx.utils.MD5Utils.MD5Encrypted;


@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Result sendMessage(Long phone) {
        String msgCode = redisTemplate.opsForValue().get(phone.toString());
        if (StringUtils.hasText(msgCode)) {
            return Result.build(null, ResultCodeEnum.SUCCESS);//避免狼费，有验证码就不发了
        }
        //1.生成验证码
        String code = RandomStringUtils.randomNumeric(4);
        //2.存入redis，key是手机号，value是验证码
//        redisTemplate.opsForValue().set(phone.toString(), code, EXPIRE_TIME, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(phone.toString(), code, 1, TimeUnit.DAYS);
        //3.向手机好发送验证码
        sendMessageCode(phone, code);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //发送验证码
    private void sendMessageCode(Long phone, String code) {
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "30024370f3844a7f95e505fb7293edec";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "code:" + code);
        bodys.put("template_id", "CST_ptdie100");  //注意，CST_ptdie100该模板ID仅为调试使用，调试结果为"status": "OK" ，即表示接口调用成功，然后联系客服报备自己的专属签名模板ID，以保证短信稳定下发
        bodys.put("phone_number", phone.toString());


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response =
                    HttpUtils.doPost(host, path, headers, querys, bodys);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            System.out.println(result);
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Result register(UserRegisterDto userRegisterDto) {
        //根据dto传递过来的查询redis验证码
        String code = redisTemplate.opsForValue().get(userRegisterDto.getUsername());//这里用户名就是电话实际是应该传个电话过来的
        //校验验证码是否一致
        if (!code.equals(userRegisterDto.getCode())) {
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        //校验用户名是否重复
        Long count = query().eq("username", userRegisterDto.getUsername())
                .eq("is_deleted", 0).count();
        if (count.intValue() != 0) {
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
        //校验电话号码是否已经注册
        Long count1 = query().eq("phone", userRegisterDto.getUsername())
                .eq("is_deleted", 0).count();
        if (count1.intValue() != 0) {
            throw new GuiguException(ResultCodeEnum.USER_PHONE_IS_EXISTS);
        }
        //存入数据库
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userRegisterDto.getUsername());
        userInfo.setPhone(userRegisterDto.getUsername());
        userInfo.setPassword(MD5Encrypted(userRegisterDto.getPassword())); //密码加密
        userInfo.setNickName(userRegisterDto.getNickName());
        //删除验证码
        //redisTemplate.delete(userRegisterDto.getUsername()); //先不删狼费，哈哈哈
        save(userInfo);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result login(UserLoginDto userLoginDto) {
        //查询用户
        UserInfo info = query().eq("phone", userLoginDto.getUsername())
                .eq("is_deleted", 0).one();
        //校验用户是否存在
        if (info == null) {
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_NOT_EXISTS);
        }
        //校验密码
        String inputPassword = MD5Encrypted(userLoginDto.getPassword());
        if (!(inputPassword.equals(info.getPassword()))) {
            throw new GuiguException(ResultCodeEnum.PASSWORD_WRONG);
        }
        //校验账户状态
        if (info.getStatus() != null && info.getStatus() == 0) {
            throw new GuiguException(ResultCodeEnum.ACCOUNT_STOP);
        }
        //生成redis放入redis
        String token = USER_INFO_KEY + UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set(token, JSONUtil.toJsonStr(info), 1L, TimeUnit.DAYS);
        return Result.build(token, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result getCurrentUserInfo(String token) {
        //从threadLocal获取用户信息
        //里面没有的不会放行
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(userInfo, userInfoVo);
        //返回用户信息
        return Result.build(userInfoVo, ResultCodeEnum.SUCCESS);
    }
}
