package com.atguigu.spzx.pay.controller;

import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.pay.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order/alipay/")
public class PaymentController {
    @Autowired
    private PaymentInfoService paymentInfoService;

    @GetMapping("submitAlipay/{orderNo}")
    public Result submitAlipay(@PathVariable("orderNo") String orderNo) {
       return paymentInfoService.savePaymentInfo(orderNo);
    }
}
