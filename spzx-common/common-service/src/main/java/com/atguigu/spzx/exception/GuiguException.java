package com.atguigu.spzx.exception;

import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import lombok.Data;

//对需要自定义的业务异常我们进行一个统一的封装
@Data
public class GuiguException extends RuntimeException {

    private Integer code;
    private String message;
    private ResultCodeEnum resultCodeEnum;

    public GuiguException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }
}
