package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.ProductUnitMapper;
import com.atguigu.spzx.manager.service.ProductUnitService;
import com.atguigu.spzx.model.entity.base.ProductUnit;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductUnitServiceImpl extends ServiceImpl<ProductUnitMapper, ProductUnit> implements ProductUnitService {
    @Override
    public List<ProductUnit> findAll() {
        List<ProductUnit> productUnits = query().eq("is_deleted", 0).list();
        return productUnits;
    }
}
