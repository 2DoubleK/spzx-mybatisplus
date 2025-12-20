package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductDetailsMapper extends BaseMapper<ProductDetails> {
    void saveProductDetails(ProductDetails productDetails);

    void updateProductDetails(ProductDetails productDetails);

    void deleteProductDetailsByProductId(Long productId);

    void updateImgsById(@Param("urls") String urls, @Param("product_id") Long product_id);
}
