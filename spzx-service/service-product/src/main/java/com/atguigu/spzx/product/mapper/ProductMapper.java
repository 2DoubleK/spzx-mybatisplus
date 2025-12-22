package com.atguigu.spzx.product.mapper;

import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    List<ProductSku> findProductSkuBySaleNum10();

    List<Product> findPageWhere(Long offset, Long limit, Long brandId, Long category1Id, Long category2Id, Long category3Id);

    Integer countNumPageWhere(Long offset, Long limit, Long brandId, Long category1Id, Long category2Id, Long category3Id);
}
