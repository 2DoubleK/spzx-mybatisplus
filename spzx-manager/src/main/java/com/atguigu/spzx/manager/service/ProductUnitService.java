package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.entity.base.ProductUnit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProductUnitService extends IService<ProductUnit> {
    List<ProductUnit> findAll();
}
