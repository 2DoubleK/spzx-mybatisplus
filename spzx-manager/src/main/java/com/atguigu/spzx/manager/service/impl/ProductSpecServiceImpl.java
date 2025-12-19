package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.ProductSpecMapper;
import com.atguigu.spzx.manager.service.ProductSpecService;
import com.atguigu.spzx.model.entity.product.ProductSpec;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductSpecServiceImpl extends ServiceImpl<ProductSpecMapper, ProductSpec> implements ProductSpecService {
    @Override
    public Result findAllSpec(Long page, Long limit) {
        Page<ProductSpec> specPage = query().eq("is_deleted", 0).page(new Page<>(page, limit));
        return Result.build(specPage, ResultCodeEnum.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveBrand(ProductSpec productSpec) {
        save(productSpec);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateBrand(ProductSpec productSpec) {
        updateById(productSpec);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteBrand(Long id) {
        update().set("is_deleted",1).eq("id",id).update();
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public List<ProductSpec> findAll() {
        List<ProductSpec> productSpecs = query().eq("is_deleted", 0).orderByDesc("id").list();
        return productSpecs;
    }
}
