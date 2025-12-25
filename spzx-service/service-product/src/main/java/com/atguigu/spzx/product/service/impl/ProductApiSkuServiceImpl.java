package com.atguigu.spzx.product.service.impl;

import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.product.service.ProductService;
import com.atguigu.spzx.service.client.service.ProductApiSkuService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@DubboService
public class ProductApiSkuServiceImpl implements ProductApiSkuService {

    @Autowired
    private ProductService productService;

    @Override
    public ProductSku getSkuBySkuId(Long skuId) {
        return productService.getSkuBySkuId(skuId);
    }

    @Override
    public Map<Long, Integer> getStock(List<Long> skuIdList) {
        return productService.getStock(skuIdList);
    }

    @Override
    public Boolean updateStorage(Map<Long, Integer> updateStock) {
        return productService.updateStock(updateStock);
    }
}
