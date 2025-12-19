package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.mapper.ProductMapper;
import com.atguigu.spzx.manager.service.ProductService;
import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductSpec;
import com.atguigu.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product/product")
@Tag(name = "商品管理接口")
public class ProductController {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductService productService;

    @GetMapping("{page}/{limit}")
    public Result findAll(@PathVariable("page") Long page,
                          @PathVariable("limit") Long limit,
                          ProductDto productDto) {
        return productService.findAll(page, limit,productDto);
    }

    @PostMapping("/save")
    @Operation(summary = "保存品牌")
    public Result findBrands(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/updateById")
    @Operation(summary = "更新接口")
    public Result updateBrand(@RequestBody Product product) {
        return productService.updateProductById(product);
    }

    @DeleteMapping("/deleteById/{id}")
    @Operation(summary = "删除接口")
    public Result updateBrand(@PathVariable("id") Long id) {
        return productService.deleteProductById(id);
    }


}
