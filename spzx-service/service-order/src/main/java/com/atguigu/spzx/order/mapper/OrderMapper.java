package com.atguigu.spzx.order.mapper;

import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper  extends BaseMapper<OrderInfo> {
    List<OrderInfo> findOrderByPgae(Integer offset, Integer limit, Integer orderStatus, Long user_id);
    List<OrderInfo> findOrderByPage(@Param("orderStatus") Integer orderStatus,Long user_id);

    Integer countOrder(Integer orderStatus, Long user_id);
}
