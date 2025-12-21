package com.atguigu.spzx.product.mapper;

import com.atguigu.spzx.model.entity.product.ProductSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {
    List<ProductSku> findPageWhere(Long offset, Long limit, String keyword, Long brandId, Long category1Id, Long category2Id, Long category3Id, Integer order);

    Integer countNumPageWhere(String keyword, Long brandId, Long category1Id, Long category2Id, Long category3Id);
}
