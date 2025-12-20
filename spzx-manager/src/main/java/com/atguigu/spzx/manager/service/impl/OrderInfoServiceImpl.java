package com.atguigu.spzx.manager.service.impl;

import cn.hutool.core.date.DateUtil;
import com.atguigu.spzx.manager.mapper.order.OrderInfoMapper;
import com.atguigu.spzx.manager.mapper.order.OrderStatisticsMapper;
import com.atguigu.spzx.manager.service.OrderInfoService;
import com.atguigu.spzx.model.dto.order.OrderStatisticsDto;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.order.OrderStatistics;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.order.OrderStatisticsVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    @Override
    public Result getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto) {
        //1.這個實體類傳遞過來的是起始時間和結束時間
        // 2. 构建查询条件：订单日期在[起始时间, 结束时间]范围内
        boolean hasBegin = orderStatisticsDto.getCreateTimeBegin() != null && orderStatisticsDto.getCreateTimeBegin() != "";
        boolean hasEnd = orderStatisticsDto.getCreateTimeEnd() != null && orderStatisticsDto.getCreateTimeEnd() != "";
        List<OrderStatistics> list = orderStatisticsMapper.selectList(new LambdaQueryWrapper<OrderStatistics>()
                // 起始时间不为空时，订单日期 >= 起始时间
                .ge(hasBegin, OrderStatistics::getOrderDate, orderStatisticsDto.getCreateTimeBegin())
                // 结束时间不为空时，订单日期 <= 结束时间
                .le(hasEnd, OrderStatistics::getOrderDate, orderStatisticsDto.getCreateTimeEnd())
                .orderByAsc(OrderStatistics::getOrderDate)
        );
        //2.根據起始時間和結束時間查詢統計表然後返回
        List<String> dateList = list.stream()
                .map(os -> {
                    // 空值处理：若日期为 null，返回空字符串或默认值
                    if (os.getOrderDate() == null) {
                        return ""; // 或 return "未知日期"
                    }
                    // 显式返回格式化后的字符串
                    return DateUtil.format(os.getOrderDate(), "yyyy-MM-dd");
                })
                .collect(Collectors.toList());
        List<BigDecimal> amountList = list.stream().map(os -> {
            return os.getTotalAmount();
        }).collect(Collectors.toList());
        //3.返回的list集合就是對應json的數組
        OrderStatisticsVo orderStatisticsVo = new OrderStatisticsVo();
        orderStatisticsVo.setAmountList(amountList);
        orderStatisticsVo.setDateList(dateList);
        return Result.build(orderStatisticsVo, ResultCodeEnum.SUCCESS);
    }
}
