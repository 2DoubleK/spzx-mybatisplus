package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.product.CategoryBrandDto;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.entity.product.CategoryBrand;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CategoryBrandService extends IService<CategoryBrand> {

    Result<PageInfo<CategoryBrand>> findAll(Integer page, Integer limit, CategoryBrandDto categoryBrandDto);

    Result updateCB(CategoryBrand categoryBrandDto);

    Result deleteCB(Long id);

    Result saveCB(CategoryBrand categoryBrandDto);

    List<Brand> findBrandByCategoryId(Long categoryId);
}
