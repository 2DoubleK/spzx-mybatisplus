package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProductService extends IService<Product> {
    Result findAll(Long page, Long limit, ProductDto productDto);

    Result saveProduct(Product product);

    Result updateProductById(Product product);

    Result deleteProductById(Long id);
}
