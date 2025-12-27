package com.atguigu.spzx.pay.service.impl;

import com.atguigu.spzx.model.entity.pay.PaymentInfo;
import com.atguigu.spzx.pay.service.PaymentInfoService;
import com.atguigu.spzx.service.user.client.service.PaymentInfoApiService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DubboService
public class PaymentInfoApiServiceImpl implements PaymentInfoApiService {
    @Autowired
    private PaymentInfoService paymentInfoService;

//    @Override
//    public PaymentInfo savePaymentInfo(String orderNo) {
//        return paymentInfoService.savePaymentInfo(orderNo);
//    }
}
