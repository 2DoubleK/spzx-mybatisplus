package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysRoleMenuService;
import com.atguigu.spzx.model.dto.system.AssginMenuDto;
import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/system/sysRoleMenu")
public class SysRoleMenuController {
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Operation(summary = "查询所有菜单和角色已经分配的菜单")
    @GetMapping("findSysRoleMenuByRoleId/{roleId}")
    public Result findSysRoleMenuByRoleId(@PathVariable("roleId") Long roleId) {
        return sysRoleMenuService.findSysRoleMenuByRoleId(roleId);
    }

    @Operation(summary = "为角色分配菜单")
    @PostMapping("assignMenuByRoleId")
    public Result assignMenuByRoleId(@RequestBody AssginMenuDto assginMenuDto) {
        return sysRoleMenuService.assignMenuByRoleId(assginMenuDto);
    }
}
