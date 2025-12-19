package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.CategoryBrandMapper;
import com.atguigu.spzx.manager.service.CategoryBrandService;
import com.atguigu.spzx.model.dto.product.CategoryBrandDto;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.entity.product.CategoryBrand;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryBrandServiceImpl extends ServiceImpl<CategoryBrandMapper, CategoryBrand> implements CategoryBrandService {
    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    @Override
    public Result findAll(Integer page, Integer limit, CategoryBrandDto categoryBrandDto) {
        // 1. 参数兜底
        if (page == null || page < 1) {
            page = 1; //偏移量offset应该是page-1，避免0-1
        }
        if (limit == null || limit < 1) {
            limit = 10;
        }
        Long categoryId = categoryBrandDto.getCategoryId();
        Long brandId = categoryBrandDto.getBrandId();
        // 在ServiceImpl的findAll方法中添加
        if (brandId != null && brandId.toString().trim().isEmpty()) {
            categoryBrandDto.setBrandId(null);
        }
        if (categoryId != null && categoryId.toString().trim().isEmpty()) {
            categoryId = null;
        }
        int offset = (page - 1) * limit;
        List<CategoryBrand> categoryBrands = categoryBrandMapper.queryCBs(offset, limit, categoryId, brandId);//offset初始位置是-1的
        Integer total = categoryBrandMapper.countNums(categoryId, brandId);
        //封装为Page类型
        Page<CategoryBrand> categoryBrandPage = new Page<>();
        categoryBrandPage.setCurrent(page);
        categoryBrandPage.setSize(limit);
        categoryBrandPage.setTotal(total);
        categoryBrandPage.setRecords(categoryBrands);
        return Result.build(categoryBrandPage, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result updateCB(CategoryBrand categoryBrandDto) {
        updateById(categoryBrandDto);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result deleteCB(Long id) {
        update().set("is_deleted", 1).eq("id", id).update();
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public List<Brand> findBrandByCategoryId(Long categoryId) {
        List<Brand> list = categoryBrandMapper.findBrandByCategoryId(categoryId);
        return list;
    }

    @Override
    public Result saveCB(CategoryBrand categoryBrandDto) {
        save(categoryBrandDto);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
