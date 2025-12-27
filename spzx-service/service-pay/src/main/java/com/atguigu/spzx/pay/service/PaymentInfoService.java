package com.atguigu.spzx.pay.service;

import com.atguigu.spzx.model.entity.pay.PaymentInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PaymentInfoService extends IService<PaymentInfo> {
    Result savePaymentInfo(String orderNo);
}
