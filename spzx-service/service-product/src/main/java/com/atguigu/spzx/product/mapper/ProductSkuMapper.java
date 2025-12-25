package com.atguigu.spzx.product.mapper;

import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.order.ProductSkuStockVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {
    List<ProductSku> findPageWhere(
            @Param("offset") Long offset,
            @Param("limit") Long limit,
            @Param("keyword") String keyword,
            @Param("brandId") Long brandId,
            @Param("category1Id") Long category1Id,
            @Param("category2Id") Long category2Id,
            @Param("category3Id") Long category3Id,
            @Param("order") Integer order
    );

    Integer countNumPageWhere(
            @Param("keyword") String keyword,
            @Param("brandId") Long brandId,
            @Param("category1Id") Long category1Id,
            @Param("category2Id") Long category2Id,
            @Param("category3Id") Long category3Id
    );


    List<ProductSkuStockVo> selectStockBySkuId(@Param("skuIdList") List<Long> skuIdList);
}
