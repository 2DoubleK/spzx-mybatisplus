package com.atguigu.spzx.common.log.service;

import com.atguigu.spzx.model.entity.system.SysOperLog;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AsyncOperLogService extends IService<SysOperLog> {
    void saveLog(SysOperLog sysOperLog);
}
