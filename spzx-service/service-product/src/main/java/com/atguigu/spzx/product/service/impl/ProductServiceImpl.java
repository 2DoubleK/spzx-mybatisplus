package com.atguigu.spzx.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.PageVO;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.ProductItemVo;
import com.atguigu.spzx.model.vo.order.ProductSkuStockVo;
import com.atguigu.spzx.product.mapper.ProductDetailsMapper;
import com.atguigu.spzx.product.mapper.ProductMapper;
import com.atguigu.spzx.product.mapper.ProductSkuMapper;
import com.atguigu.spzx.product.service.ProductService;
import com.atguigu.spzx.service.user.client.service.UserBrowseHistoryApiService;
import com.atguigu.spzx.utils.AuthContextUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductDetailsMapper productDetailsMapper;
    @DubboReference
    private UserBrowseHistoryApiService userBrowseHistoryApiService;

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

        PageVO<ProductSku> pageVO = new PageVO<>(productSkus, total, page, limit);
        return Result.build(pageVO, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result queryProductItem(Long skuId) {
        ProductItemVo productItemVo = null;
        try {
            productItemVo = new ProductItemVo();
            //也就是要从三张表里查信息，product、product_sku、product_details
            //1.查询商品的sku信息
            ProductSku productSku = productSkuMapper.selectOne(new LambdaQueryWrapper<ProductSku>().eq(ProductSku::getId, skuId));
            Long productId = productSku.getProductId();

            //2.查询商品信息
            Product product = productMapper.selectOne(new QueryWrapper<Product>().eq("id", productId));
            //3.查询商品详情信息
            ProductDetails productDetails = productDetailsMapper.selectOne(new LambdaQueryWrapper<ProductDetails>().eq(ProductDetails::getProductId, productId));
            //4.查询同一商品id的所有商品规格信息集合
            Map<String, Object> skuSpecMap = new HashMap<>();
            //根据商品id获取商品sku列表
            List<ProductSku> productSkus = productSkuMapper.selectList(new LambdaQueryWrapper<ProductSku>().eq(ProductSku::getProductId, productId));
            productSkus.stream().map(item -> {
                return skuSpecMap.put(item.getSkuSpec(), item.getId());
            });
            //sku信息
            productItemVo.setProductSku(productSku);
            //商品信息
            productItemVo.setProduct(product);
            //商品id的所有商品规格信息集合
            productItemVo.setSkuSpecValueMap(skuSpecMap);
            //商品轮播图列表
            productItemVo.setSliderUrlList(Arrays.asList(product.getSliderUrls().split(",")));
            //商品详情图片列表
            productItemVo.setDetailsImageUrlList(Arrays.asList(productDetails.getImageUrls().split(",")));
            //规格数据
            productItemVo.setSpecValueList(JSON.parseArray(product.getSpecValue()));
            //TTOD :远程写入用户浏览记录
            Long user_id = AuthContextUtil.getUserInfo().getId();
            userBrowseHistoryApiService.addBrowseHistory(skuId,user_id );
        } catch (Exception e) {
            log.error("{}", e);
            throw new RuntimeException(e);
        }
        return Result.build(productItemVo, ResultCodeEnum.SUCCESS);
    }

    @Override
    public ProductSku getSkuBySkuId(Long skuId) {
        ProductSku productSku = productSkuMapper.selectOne(new QueryWrapper<ProductSku>().eq("id", skuId));
        return productSku;
    }

    @Override
    public Map<Long, Integer> getStock(List<Long> skuIdList) {
        Map<Long, Integer> stockMap = new HashMap<>();
        if (CollUtil.isEmpty(skuIdList)) {
            return stockMap; // 空列表直接返回，避免无效查询
        }
        // 1. 批量查询符合条件的sku库存（仅1次数据库交互）
        List<ProductSkuStockVo> vos = productSkuMapper.selectStockBySkuId(skuIdList);
        vos.forEach(item -> {
            stockMap.put(item.getSkuId(), item.getStock());
        });
        return stockMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStock(Map<Long, Integer> updateStock) {
        for (Map.Entry<Long, Integer> entry : updateStock.entrySet()) {
            Long skuId = entry.getKey();
            Integer newStock = entry.getValue();
            // 1. 查当前skuid的库存
            ProductSku sku = productSkuMapper.selectById(skuId);
            if (sku == null) {
                throw new RuntimeException("SKU 不存在，skuId=" + skuId);
            }
            Integer oldStock = sku.getStockNum();
            if (newStock < 0) {
                throw new RuntimeException("库存不能为负数，skuId=" + skuId);
            }
            // 2. 条件更新
            UpdateWrapper<ProductSku> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", skuId)
                    .eq("stock_num", oldStock)  //比对新旧值
                    .set("stock_num", newStock);

            int rows = productSkuMapper.update(null, wrapper);
            if (rows != 1) {
                throw new RuntimeException("库存更新失败（库存已被修改），skuId=" + skuId);
            }
        }
        return true;
    }

}
