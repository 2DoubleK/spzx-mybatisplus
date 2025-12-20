package com.atguigu.spzx.model.entity.product;

import com.atguigu.spzx.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("product_details")
public class ProductDetails extends BaseEntity {

	private Long productId;
	private String imageUrls;

}