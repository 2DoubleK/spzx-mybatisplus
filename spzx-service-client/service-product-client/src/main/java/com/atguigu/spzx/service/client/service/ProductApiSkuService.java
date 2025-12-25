package com.atguigu.spzx.service.client.service;

import com.atguigu.spzx.model.entity.product.ProductSku;

import java.util.List;
import java.util.Map;

public interface ProductApiSkuService {
    ProductSku getSkuBySkuId(Long skuId);
    Map<Long,Integer> getStock(List<Long> skuIdList);

    Boolean updateStorage(Map<Long, Integer> updateStock);
}
