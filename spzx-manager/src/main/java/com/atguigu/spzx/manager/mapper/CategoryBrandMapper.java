package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.entity.product.CategoryBrand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryBrandMapper extends BaseMapper<CategoryBrand> {
    List<CategoryBrand> queryCBs(@Param("offset") Integer offset,
                                 @Param("limit") Integer limit,
                                 @Param("categoryId") Long categoryId,
                                 @Param("brandId")Long brandId);

    Integer countNums(@Param("categoryId") Long categoryId,
                   @Param("brandId")Long brandId);

    List<Brand> findBrandByCategoryId(Long categoryId);
}
