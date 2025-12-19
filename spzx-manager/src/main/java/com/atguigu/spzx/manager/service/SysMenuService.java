package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysMenuService  extends IService<SysMenu> {
    Result findNodes();

    Result saveNode(SysMenu sysMenu);

    Result updateMenu(SysMenu sysMenu);

    Result deleteMenu(Long id);

    Result findMenuByUserId();
}
