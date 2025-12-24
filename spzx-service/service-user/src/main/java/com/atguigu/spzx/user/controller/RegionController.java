package com.atguigu.spzx.user.controller;

import com.atguigu.spzx.model.entity.base.Region;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.user.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/region")
public class RegionController {
    @Autowired
    private RegionService regionService;


    @GetMapping("/findByParentCode/{parentCode}")
    public Result findByParentCode(@PathVariable("parentCode")Long parentCode){
       return regionService.findByParentCode(parentCode);
    }
}
