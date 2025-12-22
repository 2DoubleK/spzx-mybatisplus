package com.atguigu.spzx.user.mapper;

import com.atguigu.spzx.model.entity.system.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.stream.BaseStream;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
}
