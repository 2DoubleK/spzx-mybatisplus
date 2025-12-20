package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.CategoryBrandService;
import com.atguigu.spzx.model.dto.product.CategoryBrandDto;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.entity.product.CategoryBrand;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product/categoryBrand")
public class CategoryBrandController {
    @Autowired
    private CategoryBrandService categoryBrandService;

    @GetMapping("/{page}/{limit}")
    public Result<PageInfo<CategoryBrand>> findByPage(@PathVariable Integer page,
                                                      @PathVariable Integer limit,
                                                      CategoryBrandDto categoryBrandDto) {
        return categoryBrandService.findAll(page, limit, categoryBrandDto);
    }

    @PostMapping("/save")
    @Operation(summary = "保存品牌")
    public Result findBrands(@RequestBody CategoryBrand categoryBrandDto) {
        return categoryBrandService.saveCB(categoryBrandDto);
    }

    @PutMapping("/updateById")
    @Operation(summary = "更新接口")
    public Result updateBrand(@RequestBody CategoryBrand categoryBrandDto) {
        return categoryBrandService.updateCB(categoryBrandDto);
    }

    @DeleteMapping("/deleteById/{id}")
    @Operation(summary = "删除接口")
    public Result updateBrand(@PathVariable("id") Long id) {
        return categoryBrandService.deleteCB(id);
    }

    //根据分类查询品牌数据
    @GetMapping("/findBrandByCategoryId/{categoryId}")
    public Result findBrandByCategoryId(@PathVariable Long categoryId) {
        List<Brand> brandList = categoryBrandService.findBrandByCategoryId(categoryId);
        return Result.build(brandList, ResultCodeEnum.SUCCESS);
    }
}
