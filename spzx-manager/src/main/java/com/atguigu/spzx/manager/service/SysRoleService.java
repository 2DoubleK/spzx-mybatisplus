package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysRoleService extends IService<SysRole> {
    Result findByPage(SysRoleDto sysRoleDto, Integer current, Integer limit);

    Result saveSysRole(SysRole sysRole);

    Result deleteSysRoleById(Long id);
}
