package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.mapper.ProductMapper;
import com.atguigu.spzx.manager.service.ProductService;
import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
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
    //根据商品id查询商品信息
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable("id")Long id){
        return productService.getProductById(id);
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

    //商品审核
    @GetMapping("/updateAuditStatus/{id}/{auditStatus}")
    public Result updateAuditStatus(@PathVariable Long id, @PathVariable Integer auditStatus) {
        productService.updateAuditStatus(id, auditStatus);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        productService.updateStatus(id, status);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
}
