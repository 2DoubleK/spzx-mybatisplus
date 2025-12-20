package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.ProductSpecService;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.entity.product.ProductSpec;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product/productSpec")
public class ProductSpecController {
    @Autowired
    private ProductSpecService productSpecService;

    @GetMapping("{page}/{limit}")
    public Result findAll(@PathVariable("page") Long page,
                          @PathVariable("limit") Long limit) {
        return productSpecService.findAllSpec(page, limit);
    }

    @PostMapping("/save")
    @Operation(summary = "保存品牌")
    public Result findBrands(@RequestBody ProductSpec productSpec) {
        return productSpecService.saveBrand(productSpec);
    }

    @PutMapping("/updateById")
    @Operation(summary = "更新接口")
    public Result updateBrand(@RequestBody ProductSpec productSpec) {
        return productSpecService.updateBrand(productSpec);
    }

    @DeleteMapping("/deleteById/{id}")
    @Operation(summary = "删除接口")
    public Result updateBrand(@PathVariable("id") Long id) {
        return productSpecService.deleteBrand(id);
    }

    //查询所有商品规格接口
    @GetMapping("findAll")
    public Result findAll() {
        List<ProductSpec> list = productSpecService.findAll();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }
}
