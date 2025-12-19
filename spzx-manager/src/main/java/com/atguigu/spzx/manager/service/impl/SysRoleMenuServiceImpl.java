package com.atguigu.spzx.manager.service.impl;

import cn.hutool.core.convert.Convert;
import com.atguigu.spzx.manager.mapper.SysRoleMapper;
import com.atguigu.spzx.manager.mapper.SysRoleMenuMapper;
import com.atguigu.spzx.manager.service.SysMenuService;
import com.atguigu.spzx.manager.service.SysRoleMenuService;
import com.atguigu.spzx.model.dto.system.AssginMenuDto;
import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.entity.system.SysRoleMenu;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    //查询所有菜单和角色已经分配的菜单
    @Override
    public Result findSysRoleMenuByRoleId(Long roleId) {
        // 1. 查询所有菜单
        List<SysMenu> list = Convert.toList(SysMenu.class, sysMenuService.findNodes().getData());
        // 2. 查询当前角色关联的SysRoleMenu列表
        List<SysRoleMenu> roleMenuList = query()
                .select("menu_id")
                .eq("role_id", roleId)
                .eq("is_deleted", 0)
                .eq("is_half",0)
                .list();
        // 3. 提取每个对象的menuId属性
        List<Long> roleMenuIds = roleMenuList.stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toList());
        // 4. 封装数据：roleMenuIds是纯ID数组
        Map<String, Object> map = new HashMap<>();
        map.put("sysMenuList", list);
        map.put("roleMenuIds", roleMenuIds); // 现在是[1,2,31,3,4,21]
        return Result.build(map, ResultCodeEnum.SUCCESS);
    }

    //为角色分配菜单
    //AssignMenu里isHalf标记为是否全选
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result assignMenuByRoleId(AssginMenuDto assginMenuDto) {
        //删除当前角色已经分配的菜单
        sysRoleMenuMapper.delete(new QueryWrapper<SysRoleMenu>().eq("role_id", assginMenuDto.getRoleId()));
        //保存已经分配的菜单给该角色
        List<Map<String, Number>> menuIdList = assginMenuDto.getMenuIdList();
        if (menuIdList != null && menuIdList.size() > 0) {
            //xml文件里使用foreach遍历
            sysRoleMenuMapper.doAssign(assginMenuDto);
        }
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
