package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.entity.product.ProductSpec;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProductSpecService extends IService<ProductSpec> {
    Result findAllSpec(Long page, Long limit);


    Result saveBrand(ProductSpec productSpec);

    Result updateBrand(ProductSpec productSpec);

    Result deleteBrand(Long id);

    List<ProductSpec> findAll();
}
