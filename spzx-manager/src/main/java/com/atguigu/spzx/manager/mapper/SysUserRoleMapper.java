package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.entity.system.SysRoleUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysRoleUser> {

    void deleteByUserId(Long userId);

    void doAssign(Long userId, Long roleId);

    List<Long> querySysRoleByUserId(Long id);

    List<SysMenu> findMenuByUserId(Long id);


    void updateSysRoleMenuIsHalf(Long menuId);
}
