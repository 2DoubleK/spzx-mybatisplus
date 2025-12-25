package com.atguigu.spzx.user.controller;

import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.user.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "会员用户接口")
@RestController
@RequestMapping("api/user/userInfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "会员注册")
    @PostMapping("register")
    public Result register(@RequestBody UserRegisterDto userRegisterDto) {
        return userInfoService.register(userRegisterDto);
    }

    @Operation(summary = "会员登录")
    @PostMapping("login")
    public Result login(@RequestBody UserLoginDto userLoginDto) {
        return userInfoService.login(userLoginDto);
    }

    //auth/getCurrentUserInfo
    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/auth/getCurrentUserInfo")
    public Result getCurrentUserInfo(HttpServletRequest request) {
        return userInfoService.getCurrentUserInfo(request.getHeader("Token"));
    }

    // 用户收藏商品
    @Operation(summary = "收藏商品")
    @GetMapping("/auth/collect/{skuId}")
    public Result collect(@PathVariable("skuId")Long skuId) {
        return userInfoService.collect(skuId);
    }

    //   获取用户浏览信息
    @Operation(summary = "浏览列表")
    @GetMapping("/auth/findUserBrowseHistoryPage/{page}/{limit}")
    public Result findUserBrowseHistoryPage(@PathVariable("page")Long page,
                                            @PathVariable("limit")Long limit) {
        return userInfoService.findUserBrowseHistoryPage(page,limit);
    }
    //  获取用户收藏信息，findUserCollectPage
    @Operation(summary = "收藏商品列表")
    @GetMapping("/auth/findUserCollectPage/{page}/{limit}")
    public Result findUserCollectPage(@PathVariable("page")Long page,
                                            @PathVariable("limit")Long limit) {
        return userInfoService.findUserCollectPage(page,limit);
    }
    // isCollect/4

    @Operation(summary = "收藏商品")
    @GetMapping("isCollect/{skuId}")
    public Result isCollect(@PathVariable("skuId")Long skuId) {
        return userInfoService.isCollect(skuId);
    }
}