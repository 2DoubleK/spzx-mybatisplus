package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.ProductMapper;
import com.atguigu.spzx.manager.service.ProductService;
import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Override
    public Result findAll(Long page, Long limit, ProductDto productDto) {
        Long offset = (page - 1) * limit;

        List<Product> products = productMapper.findFindPageWhere(
                offset,
                limit,
                productDto.getBrandId(),
                productDto.getCategory1Id(),
                productDto.getCategory2Id(),
                productDto.getCategory3Id());
        Integer total = productMapper.countNumPageWhere(offset,
                limit,
                productDto.getBrandId(),
                productDto.getCategory1Id(),
                productDto.getCategory2Id(),
                productDto.getCategory3Id());

        Page<Product> productPage = new Page<>();
        productPage.setSize(limit);
        productPage.setCurrent(page);
        productPage.setRecords(products);
        productPage.setTotal(total);
        return Result.build(productPage, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result saveProduct(Product product) {
        return null;
    }

    @Override
    public Result updateProductById(Product product) {
        return null;
    }

    @Override
    public Result deleteProductById(Long id) {
        return null;
    }
}
