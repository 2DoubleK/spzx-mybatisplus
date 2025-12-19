package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 批量插入分类数据
     * @param categoryList 分类列表
     */
    void batchInsert(@Param("list") List<Category> categoryList);
}
