package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.baomidou.mybatisplus.extension.service.IService;


public interface SysUserService extends IService<SysUser> {
    LoginVo login(LoginDto loginDto);

    SysUser getUserInfo(String token);

    void logout(String token);

    Result<?> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto);

    Result<?> saveSysUser(SysUser sysUser);

    Result<?> deleteSysUser(Long id);


    Result<?> updateSysUser(SysUser sysUser);


    Result<?> queryAllSysRole();

    Result<?> assignSysRole(AssginRoleDto assginRoleDto);

    Result<?> querySysRoleByUserId(Long id);
}
