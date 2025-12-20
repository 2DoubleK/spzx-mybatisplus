package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductDetailsMapper extends BaseMapper<ProductDetails> {
    void updateImgsById(@Param("urls") String urls, @Param("product_id") Long product_id);

    void insertDetails(ProductDetails details);

    void updateDetails(ProductDetails details);

    void deleteDetailsByProductIdLogical(Long productId);
}
