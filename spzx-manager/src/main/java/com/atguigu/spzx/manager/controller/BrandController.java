package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.BrandService;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "品牌接口")
@RequestMapping("/admin/product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @GetMapping("/{page}/{size}")
    @Operation(summary = "获取品牌分页")
    public Result findBrands(@PathVariable("page") Integer page,
                             @PathVariable("size")Integer size){
        return brandService.findBrand(page,size);
    }

    @PostMapping("/save")
    @Operation(summary = "添加品牌")
    public Result findBrands(@RequestBody Brand brand) {
        return brandService.saveBrand(brand);
    }
    @PutMapping("/update")
    @Operation(summary = "更新接口")
    public Result updateBrand(@RequestBody Brand brand){
        return brandService.updateBrand(brand);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除接口")
    public Result updateBrand(@PathVariable("id")Long id) {
        return brandService.deleteBrand(id);
    }
    @GetMapping("/findAll")
    @Operation(summary = "获得所有品牌")
    public Result updateBrand() {
        return  Result.build(brandService.list(), ResultCodeEnum.SUCCESS);
    }
}
