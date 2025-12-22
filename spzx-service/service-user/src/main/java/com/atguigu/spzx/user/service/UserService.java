package com.atguigu.spzx.user.service;

import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<SysUser> {
    Result sendMessage(Long phone);
}
