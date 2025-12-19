package com.atguigu.spzx.common.exception;

import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error() {
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }
    //自定义异常
    @ExceptionHandler(GuiguException.class)
    @ResponseBody
    public Result guiguError(GuiguException e){
        return Result.build(null,e.getResultCodeEnum());
    }
}

