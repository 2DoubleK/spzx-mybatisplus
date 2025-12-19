package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.SysRoleMapper;
import com.atguigu.spzx.manager.service.SysRoleService;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public Result findByPage(SysRoleDto sysRoleDto, Integer current, Integer limit) {
        //有sysRoleDto.getRoleName()就将它作为where条件没有则只根据分页查询
        Page<SysRole> rolePage = lambdaQuery()
                .eq(sysRoleDto != null && sysRoleDto.getRoleName() != null && !sysRoleDto.getRoleName().trim().isEmpty(),
                        SysRole::getRoleName,  //相当于调用了SysRole的getRoleName方法
                        sysRoleDto.getRoleName().trim())
                .ne(SysRole::getIsDeleted,1)
                .orderByDesc(SysRole::getId)  //相当于调用了getId方法
                .page(new Page<>(current, limit));
        return Result.build(rolePage, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result deleteSysRoleById(Long id) {
        update().eq("id", id).set("is_deleted", 1).update();
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result saveSysRole(SysRole sysRole) {
        if (sysRole.getId() == null) {
            save(sysRole);
        } else {
            updateById(sysRole);
        }
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
