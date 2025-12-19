package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.CategoryService;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "商品分类结构")
@RestController
@RequestMapping("/admin/product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/findCategoryList/{id}")
    @Operation(summary = "查询所有分类")
    public Result findCategoryList(@PathVariable("id") Long id) {
        return categoryService.findCategoryList(id);
    }

    @GetMapping("/exportData")
    @Operation(summary = "导出数据")
    public Result exportExcel(HttpServletResponse response) {
        return categoryService.exportExcel( response);
    }

    @PostMapping("/importData")
    @Operation(summary = "导入数据")
    public Result importExcel(MultipartFile file) {
     return categoryService.importExcel(file);
    }
}
