package com.atguigu.spzx.user.controller;

import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/sms")
@Tag(name="短信接口")
public class MessageController {
    @Autowired
    private UserService userService;
    @GetMapping("/sendCode/{phone}")
    @Operation(summary = "短信发送")
    public Result sendMessage(@PathVariable("phone")Long phone){
        return userService.sendMessage(phone);
    }
}
