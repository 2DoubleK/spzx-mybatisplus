package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.BrandMapper;
import com.atguigu.spzx.manager.service.BrandService;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.utils.MinioUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    @Override
    public Result findBrand(Integer page, Integer size) {
        Page<Brand> page1 = query().eq("is_deleted", 0).page(new Page<Brand>(page, size));
        return Result.build(page1, ResultCodeEnum.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteBrand(Long id) {
        update().set("is_deleted", 1).eq("id",id).update();
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateBrand(Brand brand) {
        updateById(brand);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveBrand(Brand brand) {
        save(brand);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
