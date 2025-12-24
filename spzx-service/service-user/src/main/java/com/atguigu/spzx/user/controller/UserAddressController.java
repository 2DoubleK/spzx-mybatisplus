package com.atguigu.spzx.user.controller;

import com.atguigu.spzx.model.entity.base.Region;
import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/userAddress")
public class UserAddressController {
    @Autowired
    private UserAddressService userAddressService;
    //获取用户地址信息
    @GetMapping("/auth/findUserAddressList")
    public Result findUserAddressList(){
        return  userAddressService.findUserAddressList();
    }
    //保存地址信息
    @PostMapping("/auth/save")
    public Result saveRegion( @RequestBody UserAddress address){
        return userAddressService.saveRegion(address);
    }
}
