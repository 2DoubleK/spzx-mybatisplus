package com.atguigu.spzx.product.controller;

import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.product.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "品牌接口")
@RequestMapping("/api/product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("findAll")
    @Operation(summary = "获取全部品牌")
    public Result findAll() {
        return brandService.findAll();
    }


}
