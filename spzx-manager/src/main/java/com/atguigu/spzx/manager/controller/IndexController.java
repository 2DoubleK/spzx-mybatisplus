package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysMenuService;
import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.manager.service.ValidateCodeService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import com.atguigu.spzx.utils.AuthContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户接口") //用于实体类，标记接口分组
@RestController
@RequestMapping(value = "/admin/system/index")
//@CrossOrigin("*")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ValidateCodeService validateCodeService;
    @Autowired
    private SysMenuService sysMenuService;

    @Operation(summary = "登录接口") //用于接口方法，描述单个接口作用
    @PostMapping(value = "/login")
    public Result<LoginVo> login(@RequestBody LoginDto loginDto) {
        LoginVo loginVo = sysUserService.login(loginDto);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS); //返回统一的响应实体对象
    }
    @Operation(summary = "验证码生成接口") //用于接口方法，描述单个接口作用
    @GetMapping("generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode() {
        ValidateCodeVo validateCodeVo = validateCodeService.generateValidateCode();
        return Result.build(validateCodeVo, ResultCodeEnum.SUCCESS);
    }
    @Operation(summary = "用户信息查询接口") //用于接口方法，描述单个接口作用
    @GetMapping("getUserInfo")
    public Result getUserInfo() {

        return Result.build(AuthContextUtil.get(), ResultCodeEnum.SUCCESS);
    }
//    @GetMapping("getUserInfo")
//    public Result getUserInfo(@RequestHeader(name = "token") String token) {
//        //可以选择将参数HttpServletRequest 然后调用getHeader("token?)方法获取
//        //也可以使用注解的方式,如上:
//        //根据token查询redis的胡信息
//        //SysUser sysUser=sysUserService.getUserInfo(token);
//        SysUser sysUser= AuthContextUtil.get();
//        //返回用户信息
//        return Result.build(sysUser, ResultCodeEnum.SUCCESS);
//    }

    @Operation(summary = "注销登录接口") //用于接口方法，描述单个接口作用
    @GetMapping("logout")
    public Result logout(@RequestHeader(name = "token") String token) {
        sysUserService.logout(token);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "动态菜单") //用于接口方法，描述单个接口作用
    @GetMapping("menus")
    public Result menus() {
        return  sysMenuService.findMenuByUserId();
    }

}
