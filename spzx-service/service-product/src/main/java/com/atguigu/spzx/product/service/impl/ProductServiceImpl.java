package com.atguigu.spzx.product.service.impl;

import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.PageVO;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.product.mapper.ProductDetailsMapper;
import com.atguigu.spzx.product.mapper.ProductMapper;
import com.atguigu.spzx.product.mapper.ProductSkuMapper;
import com.atguigu.spzx.product.service.ProductService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductDetailsMapper productDetailsMapper;
    @Override
    public List<ProductSku> findProductSkuBySale() { //根据销量获取前十条记录
        return productMapper.findProductSkuBySaleNum10();
    }
    @Override
    public Result findByPage(Long page, Long limit, ProductSkuDto productSkuDto) {
        Long offset = (page - 1) * limit;

        List<ProductSku> productSkus = productSkuMapper.findPageWhere(
                offset,
                limit,
                productSkuDto.getKeyword(),
                productSkuDto.getBrandId(),
                productSkuDto.getCategory1Id(),
                productSkuDto.getCategory2Id(),
                productSkuDto.getCategory3Id(),
                productSkuDto.getOrder());
        Integer total = productSkuMapper.countNumPageWhere(
                productSkuDto.getKeyword(),
                productSkuDto.getBrandId(),
                productSkuDto.getCategory1Id(),
                productSkuDto.getCategory2Id(),
                productSkuDto.getCategory3Id());

        PageVO<ProductSku> pageVO=new PageVO<>(productSkus, total, page, limit);
        return Result.build(pageVO, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result getProductById(Long id) {
        //根据商品id查出商品信息
        Product product = query().eq("is_deleted", 0).eq("id", id).one();
        //跟据商品id查出sku信息
        List<ProductSku> productSkus = productSkuMapper.selectList(new QueryWrapper<ProductSku>()
                .eq("product_id", product.getId())
                .eq("is_deleted", 0));
        //根据id查询商品详情信息
        ProductDetails details = productDetailsMapper.selectOne(new QueryWrapper<ProductDetails>()
                .eq("product_id", product.getId())
                .eq("is_deleted", 0));
        product.setProductSkuList(productSkus);
        product.setDetailsImageUrls(details.getImageUrls());
        return Result.build(product, ResultCodeEnum.SUCCESS);
    }
}
