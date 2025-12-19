package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BrandService extends IService<Brand> {
    Result findBrand(Integer page, Integer size);

    Result saveBrand(Brand brand);

    Result deleteBrand(Long id);

    Result updateBrand(Brand brand);
}
