package com.atguigu.spzx.product.service;

import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProductService extends IService<Product> {
    List<ProductSku> findProductSkuBySale();

    Result findByPage(Long page, Long limit, ProductSkuDto productSkuDto);

    Result getProductById(Long id);
}
