package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.system.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;  // 必须导入MP的BaseMapper
import org.apache.ibatis.annotations.Mapper;

/**
 * SysUser数据访问层（Mapper）
 * 继承BaseMapper<SysUser>，获得MP的CRUD自动生成能力
 */
@Mapper  // 也可在启动类用@MapperScan替代，二选一即可
public interface SysUserMapper extends BaseMapper<SysUser> {

}