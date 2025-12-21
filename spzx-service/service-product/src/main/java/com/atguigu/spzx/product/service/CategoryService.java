package com.atguigu.spzx.product.service;

import com.atguigu.spzx.model.entity.product.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CategoryService extends IService<Category> {

    List<Category> findCategoryTree();

    List<Category> findOneCategory();
}
