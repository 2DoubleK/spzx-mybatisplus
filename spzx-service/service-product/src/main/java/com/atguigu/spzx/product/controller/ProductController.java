package com.atguigu.spzx.product.controller;


import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.product.mapper.ProductMapper;
import com.atguigu.spzx.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@Tag(name = "商品管理接口")
public class ProductController {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductService productService;

    @GetMapping("{page}/{limit}")
    @Operation(summary = "商品分页查询")
    public Result findByPage(@PathVariable("page") Long page,
                          @PathVariable("limit") Long limit,
                          ProductSkuDto productSkuDto) {
        return productService.findByPage(page, limit,productSkuDto);
    }
    //商品详情
    @Operation(summary = "商品详情")
    @GetMapping("item/{skuId}")
    public Result queryProductItem(@PathVariable("skuId")Long skuId){
        return productService.queryProductItem(skuId);
    }

}
