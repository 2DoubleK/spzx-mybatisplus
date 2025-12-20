package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.ProductDetailsMapper;
import com.atguigu.spzx.manager.mapper.ProductMapper;
import com.atguigu.spzx.manager.mapper.ProductSkuMapper;
import com.atguigu.spzx.manager.service.ProductService;
import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductDetailsMapper productDetailsMapper;


    @Override
    public Result findAll(Long page, Long limit, ProductDto productDto) {
        Long offset = (page - 1) * limit;

        List<Product> products = productMapper.findFindPageWhere(
                offset,
                limit,
                productDto.getBrandId(),
                productDto.getCategory1Id(),
                productDto.getCategory2Id(),
                productDto.getCategory3Id());
        Integer total = productMapper.countNumPageWhere(offset,
                limit,
                productDto.getBrandId(),
                productDto.getCategory1Id(),
                productDto.getCategory2Id(),
                productDto.getCategory3Id());

        Page<Product> productPage = new Page<>();
        productPage.setSize(limit);
        productPage.setCurrent(page);
        productPage.setRecords(products);
        productPage.setTotal(total);
        return Result.build(productPage, ResultCodeEnum.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveProduct(Product product) {
        //1.保存商品信息
        product.setStatus(0);
        product.setAuditStatus(0);
        productMapper.insertProduct(product);
        //2.获取商品的sku列表，保存到product_sku表里
        List<ProductSku> productSkuList = product.getProductSkuList();
        if (productSkuList != null && !productSkuList.isEmpty()) {
            for (ProductSku item : productSkuList) {
                String productSkuCode = product.getId() + "_" + UUID.randomUUID().toString().substring(0, 6);
                item.setSkuCode(productSkuCode);  //唯一商品skuid
                item.setProductId(product.getId());
                item.setSkuName(product.getName() + item.getSkuSpec());
                item.setStatus(0);
                item.setSaleNum(0);
                productSkuMapper.insertSku(item);
            }
        }
        //3.保存商品详情数据
        ProductDetails productDetails = new ProductDetails();
        productDetails.setProductId(product.getId());
        productDetails.setImageUrls(product.getDetailsImageUrls());
        productDetailsMapper.insertDetails(productDetails);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateProductById(Product product) {
        //修改product
        productMapper.updateProduct(product);

        //修改productSku
        List<ProductSku> productSkus = product.getProductSkuList();
        if (productSkus != null && !productSkus.isEmpty()) {
            List<ProductSku> dbSkus = productSkuMapper.selectList(new QueryWrapper<ProductSku>()
                    .eq("product_id", product.getId())
                    .eq("is_deleted", 0));
            for (ProductSku sku : productSkus) {
                if (sku.getId() != null) {
                    productSkuMapper.updateSku(sku);
                } else {
                    String productSkuCode = product.getId() + "_" + UUID.randomUUID().toString().substring(0, 6);
                    sku.setSkuCode(productSkuCode);
                    sku.setProductId(product.getId());
                    sku.setSkuName(product.getName() + sku.getSkuSpec());
                    sku.setStatus(0);
                    sku.setSaleNum(0);
                    productSkuMapper.insertSku(sku);
                }
            }
            for (ProductSku dbSku : dbSkus) {
                boolean found = false;
                for (ProductSku sku : productSkus) {
                    if (dbSku.getId().equals(sku.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    productSkuMapper.deleteSkuLogical(dbSku.getId());
                }
            }
        }
        //修改productDetails
        ProductDetails productDetails = new ProductDetails();
        productDetails.setProductId(product.getId());
        productDetails.setImageUrls(product.getDetailsImageUrls());
        productDetailsMapper.updateDetails(productDetails);
        
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteProductById(Long id) {
        //删除product
        productMapper.deleteProductLogical(id);

        //删除productSku
        productSkuMapper.deleteSkuByProductIdLogical(id);

        //删除productDetails
        productDetailsMapper.deleteDetailsByProductIdLogical(id);
        return Result.build(null, ResultCodeEnum.SUCCESS);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAuditStatus(Long id, Integer auditStatus) {
        boolean isWork = auditStatus == 1;
        UpdateWrapper<Product> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .set("audit_status", isWork ? 1 : -1)
                .set("audit_message", isWork ? "审核通过" : "审核不通过")
                .eq("id", id)
                .eq("is_deleted", 0); // 补充：过滤已软删除的商品
        update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        boolean isWork = status == 1;
        UpdateWrapper<Product> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .set("status", isWork ? 1 : -1)
                .eq("is_deleted", 0) // 补充：过滤已软删除的商品
                .eq("id", id)
                .eq("audit_status", 1); // 补充：仅审核通过的商品可上下架;
        update(updateWrapper);
    }
}
