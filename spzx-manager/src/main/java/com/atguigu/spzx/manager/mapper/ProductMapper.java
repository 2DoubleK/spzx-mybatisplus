package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    List<Product> findFindPageWhere(Long offset, Long limit, Long brandId, Long category1Id, Long category2Id, Long category3Id);

    Integer countNumPageWhere(Long offset, Long limit, Long brandId, Long category1Id, Long category2Id, Long category3Id);

    void insertProduct(Product product);

    void updateProduct(Product product);

    void deleteProductLogical(Long id);
}
