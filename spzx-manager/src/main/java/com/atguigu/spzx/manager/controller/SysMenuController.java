package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysMenuService;
import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/system/sysMenu")
@Tag(name = "菜单管理接口")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @Operation(summary = "查询菜单")
    @GetMapping("queryAllMenu")
    public Result findNodes(){
       return sysMenuService.findNodes();
    }

    @Operation(summary = "添加菜单")
    @PostMapping("saveMenu")
    public Result saveNode(@RequestBody SysMenu sysMenu) {
        return sysMenuService.saveNode(sysMenu);
    }
    @Operation(summary = "修改菜单")
    @PutMapping("updateMenu")
    public Result updateMenu(@RequestBody SysMenu sysMenu) {
        return sysMenuService.updateMenu(sysMenu);
    }
    @Operation(summary = "删除菜单")
    @DeleteMapping("deleteMenu/{id}")
    public Result deleteMenu(@PathVariable("id")Long id) {
        return sysMenuService.deleteMenu(id);
    }

}
