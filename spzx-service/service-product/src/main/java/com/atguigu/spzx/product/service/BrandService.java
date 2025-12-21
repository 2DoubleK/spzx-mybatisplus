package com.atguigu.spzx.product.service;

import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BrandService extends IService<Brand> {

    Result findAll();
}
