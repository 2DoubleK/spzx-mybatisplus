package com.atguigu.spzx.service.client.service;

import com.atguigu.spzx.model.entity.product.ProductSku;

public interface ProductApiSkuService {
    ProductSku getSkuBySkuId(Long skuId);
}
