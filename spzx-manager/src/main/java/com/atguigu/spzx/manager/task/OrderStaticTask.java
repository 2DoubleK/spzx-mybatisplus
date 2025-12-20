package com.atguigu.spzx.manager.task;

import cn.hutool.core.date.DateUtil;
import com.atguigu.spzx.manager.mapper.order.OrderInfoMapper;
import com.atguigu.spzx.manager.mapper.order.OrderStatisticsMapper;
import com.atguigu.spzx.model.entity.order.OrderStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class OrderStaticTask {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    //測試方法，每五秒執行一次
    @Scheduled(cron = "0 0 13 * * ?")  // 每天凌晨13点整执行
    public void testHello() {
        log.info("============================test==============================");
    }

    @Scheduled(cron = "0 0 2 * * ?")  // 每天凌晨2点整执行
    public void orderTotalAmountStatistics() {
        //1.獲取前一天日期
        String os_date = DateUtil.offsetDay(new Date(), -1).toString("yyyy-MM-dd");
        //2.根據前天的日期進行統計
        //統計前一天的交易金額
        OrderStatistics os=orderInfoMapper.selectStatisticsByDate(os_date);
        //3.寫入統計表裏
        if(os!=null){
            orderStatisticsMapper.insert(os);
        }

    }
}
