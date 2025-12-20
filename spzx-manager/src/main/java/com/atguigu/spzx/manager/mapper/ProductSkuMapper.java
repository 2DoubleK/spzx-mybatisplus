package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.ProductSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {
    void saveProductSku(ProductSku productSku);

    void updateProductSku(ProductSku productSku);

    void deleteProductSkuByProductId(Long productId);

    void deleteProductSku(Long id);
}
