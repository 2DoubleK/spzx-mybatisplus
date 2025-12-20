package com.atguigu.spzx.common.log.service.impl;

import com.atguigu.spzx.common.log.mapper.AsyncOperLogMapper;
import com.atguigu.spzx.common.log.service.AsyncOperLogService;
import com.atguigu.spzx.model.entity.system.SysOperLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsyncOperLogServiceImpl extends ServiceImpl<AsyncOperLogMapper, SysOperLog> implements AsyncOperLogService {
    @Override
    public void saveLog(SysOperLog sysOperLog) {
        save(sysOperLog);
    }
}
