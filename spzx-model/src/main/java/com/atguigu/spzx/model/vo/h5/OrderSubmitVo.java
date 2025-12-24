package com.atguigu.spzx.model.vo.h5;

import com.atguigu.spzx.model.entity.order.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OrderSubmitVo {

    private Long feightFee;

    private List<OrderItem> orderItemList;

    private String remark;

    private Long userAddressId;
}
