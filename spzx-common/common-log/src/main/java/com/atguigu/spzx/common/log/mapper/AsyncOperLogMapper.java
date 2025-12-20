package com.atguigu.spzx.common.log.mapper;

import com.atguigu.spzx.model.entity.system.SysOperLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AsyncOperLogMapper extends BaseMapper<SysOperLog> {
}
