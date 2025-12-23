package com.atguigu.spzx.product.service.impl;

import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.product.service.ProductService;
import com.atguigu.spzx.service.client.service.ProductApiSkuService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DubboService
public class ProductApiSkuServiceImpl implements ProductApiSkuService {

    @Autowired
    private ProductService productService;

    @Override
    public ProductSku getSkuBySkuId(Long skuId) {
        return productService.getSkuBySkuId(skuId);
    }
}
