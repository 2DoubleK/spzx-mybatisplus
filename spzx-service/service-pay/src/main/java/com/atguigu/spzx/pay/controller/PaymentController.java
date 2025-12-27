package com.atguigu.spzx.pay.controller;

import com.alipay.api.AlipayClient;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.pay.service.PaymentInfoService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller  //需要返回支付页面，所以不能使用RestController
@RequestMapping("/api/order/alipay/")
public class PaymentController {
    @Autowired
    private PaymentInfoService paymentInfoService;


    @GetMapping("submitAlipay/{orderNo}")
    @ResponseBody
    public Result submitAlipay(
            @Parameter(name = "orderNo", description = "订单号", required = true)
            @PathVariable("orderNo") String orderNo) {
        String form = paymentInfoService.submitAlipay(orderNo);
        return Result.build(form, ResultCodeEnum.SUCCESS);
    }
}
