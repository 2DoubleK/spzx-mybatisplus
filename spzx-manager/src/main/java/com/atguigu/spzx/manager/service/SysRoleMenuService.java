package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.system.AssginMenuDto;
import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.entity.system.SysRoleMenu;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysRoleMenuService extends IService<SysRoleMenu> {
    Result findSysRoleMenuByRoleId(Long roleId);

    Result assignMenuByRoleId(AssginMenuDto assginMenuDto);
}
