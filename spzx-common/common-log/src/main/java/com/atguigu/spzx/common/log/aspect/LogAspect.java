package com.atguigu.spzx.common.log.aspect;

import com.atguigu.spzx.common.log.anotation.Log;
import com.atguigu.spzx.common.log.service.AsyncOperLogService;
import com.atguigu.spzx.common.log.util.LogUtil;
import com.atguigu.spzx.model.entity.system.SysOperLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogAspect {
    @Autowired
    private AsyncOperLogService asyncOperLogService;
    @Around(value = "@annotation(sysLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, Log sysLog) {
        SysOperLog sysOperLog = new SysOperLog();
        //1.调用业务方法之前
        LogUtil.beforeHandleLog(sysLog,joinPoint,sysOperLog);
        //2.业务方法的调用
        Object proceed = null;
        try {
            log.info("日志打印，{},{},{},{}",sysLog.businessType(),sysLog.operatorType(),sysLog.isSaveRequestData(),sysLog.isSaveResponseData());
            proceed = joinPoint.proceed();
            //3.调用业务方法之后
            LogUtil.afterHandlLog(sysLog,joinPoint,sysOperLog,0,null);//状态和异常信息
        } catch (Throwable e) {
            e.printStackTrace();
            LogUtil.afterHandlLog(sysLog,joinPoint,sysOperLog,0,e.getMessage());//状态和异常信息
            throw new RuntimeException(e);
        }
        //4.调用service方法把日志信息添加到数据库中
        asyncOperLogService.save(sysOperLog);
        return proceed;
    }
}
