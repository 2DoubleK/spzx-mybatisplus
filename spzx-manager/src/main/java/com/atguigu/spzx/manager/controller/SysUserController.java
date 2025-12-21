package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.common.log.anotation.Log;
import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/system/sysUser")
@Tag(name = "用户管理接口")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Operation(summary = "分页查询用户")
    @PostMapping("/findByPage/{current}/{limit}")  // 改为 POST 方法
    @Log(title="分页查询用户",businessType = 1,isSaveRequestData = false,isSaveResponseData = false) //方法名、類型、是否保存請求參數、是否保存數據
    public Result<?> findByPage(
            @PathVariable("current") Integer current,
            @PathVariable("limit") Integer limit,
            @RequestBody SysUserDto sysUserDto  // POST 支持 @RequestBody 接收请求体参数
    ) {
        return sysUserService.findByPage(current, limit, sysUserDto);
    }

    @Operation(summary = "新增用户")
    @PostMapping("/saveSysUser")
    public Result<?> saveSysUser(@RequestBody SysUser sysUser) {
        return sysUserService.saveSysUser(sysUser);
    }

    @Operation(summary = "修改用户")
    @PutMapping("/updateSysUser")
    public Result<?> updateSysUser(@RequestBody SysUser sysUser) {
        return sysUserService.updateSysUser(sysUser);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/deleteSysUser/{id}")
    public Result<?> deleteSysUser(@PathVariable("id")Long id) {
        return sysUserService.deleteSysUser(id);
    }

    @Operation(summary = "为用户分配角色")
    @PutMapping ("/assignSysRole")
    public Result<?> assignSysRole(@RequestBody AssginRoleDto assginRoleDto) {
        return sysUserService.assignSysRole(assginRoleDto);
    }

    @Operation(summary = "查询所有角色")
    @GetMapping("/queryAllSysRole")
    public Result<?> queryAllSysRole() {
        return sysUserService.queryAllSysRole();
    }
    @Operation(summary = "根据id查询用户的角色")
    @GetMapping("/queryAllSysRoleByUserId/{id}")
    public Result<?> queryAllSysRoleByUserId(@PathVariable("id") Long id) {
        return sysUserService.querySysRoleByUserId(id);
    }

}
