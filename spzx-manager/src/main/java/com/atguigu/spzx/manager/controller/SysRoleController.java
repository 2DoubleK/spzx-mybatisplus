package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysRoleService;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.atguigu.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/system/sysRole")
@Tag(name = "角色接口") //用于实体类，标记接口分组
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Operation(summary="分页接口")
    @PostMapping("/findByPage/{current}/{limit}")
    public Result findByPage(@PathVariable("current")Integer current,
                             @PathVariable("limit")Integer limit,
                             @RequestBody SysRoleDto sysRoleDto){
      return   sysRoleService.findByPage(sysRoleDto,current,limit);
    }

    @PostMapping("/saveSysRole")
    @Operation(summary="新增用户")
    public Result saveSysRole(@RequestBody SysRole sysRole) {
        return sysRoleService.saveSysRole(sysRole);
    }

    @PutMapping("/updateSysRole")
    @Operation(summary = "修改用户")
    public Result updateSysRole(@RequestBody SysRole sysRole) {
        return sysRoleService.saveSysRole(sysRole);
    }

    @DeleteMapping("/deleteSysRole/{id}") // 增加路径参数id
    @Operation(summary = "删除角色") // 修正注释错误
    public Result deleteSysRole(@PathVariable Long id) { // 用@PathVariable接收ID
        // 调用Service层根据ID删除
        return sysRoleService.deleteSysRoleById(id);
    }
}
