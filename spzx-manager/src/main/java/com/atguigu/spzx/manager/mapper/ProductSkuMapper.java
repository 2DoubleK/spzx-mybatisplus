package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.ProductSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {
    void insertSku(ProductSku sku);

    void updateSku(ProductSku sku);

    void deleteSkuLogical(Long id);

    void deleteSkuByProductIdLogical(Long productId);
}
