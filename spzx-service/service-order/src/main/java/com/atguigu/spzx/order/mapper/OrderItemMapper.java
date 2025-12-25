package com.atguigu.spzx.order.mapper;

import com.atguigu.spzx.model.entity.order.OrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    void insertBatch(List<OrderItem> finalOrderItemList);

    List<OrderItem> findItemsByOrderIds(@Param("ids") List<Long> ids);

    List<OrderItem> findItemsByOrderId(Long id);
}
