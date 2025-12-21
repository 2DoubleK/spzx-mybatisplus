package com.atguigu.spzx.product.service.impl;


import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.product.mapper.BrandMapper;
import com.atguigu.spzx.product.service.BrandService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    @Override
    public Result findAll() {
        List<Brand> brands = query().eq("is_deleted", 0).orderByDesc("id").list();
        return Result.build(brands, ResultCodeEnum.SUCCESS);
    }
}
