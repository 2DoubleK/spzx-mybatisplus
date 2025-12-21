package com.atguigu.spzx.product.mapper;

import com.atguigu.spzx.model.entity.product.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    List<Category> findAllTopCate();
}
