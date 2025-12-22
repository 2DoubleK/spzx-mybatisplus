package com.atguigu.spzx.user.service;

import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserInfoService extends IService<UserInfo> {
    Result sendMessage(Long phone);

    Result register(UserRegisterDto userRegisterDto);

    Result login(UserLoginDto userLoginDto);

    Result getCurrentUserInfo(String token);
}
