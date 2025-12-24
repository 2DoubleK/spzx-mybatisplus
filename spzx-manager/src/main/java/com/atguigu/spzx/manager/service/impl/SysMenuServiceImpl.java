package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.Utils.MenuHelper;
import com.atguigu.spzx.manager.mapper.SysMenuMapper;
import com.atguigu.spzx.manager.mapper.SysUserRoleMapper;
import com.atguigu.spzx.manager.service.SysMenuService;
import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.utils.AuthContextUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public Result findNodes() {
        //查询所有菜单，返回list集合
        List<SysMenu> list = query().eq("is_deleted", 0).orderByAsc("sort_value").list();
        //调用工具类，转换为符合element格式的风格
        List<SysMenu> sysMenus = MenuHelper.buildTree(list);
        return Result.build(sysMenus, ResultCodeEnum.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveNode(SysMenu sysMenu) {
        save(sysMenu);
        //新加以恶搞子菜单，将父菜单的ishalf改为1
        updateIsHalfById(sysMenu);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    private void updateIsHalfById(SysMenu sysMenu) {
        SysMenu parentMenu = query().eq("id", sysMenu.getParentId()).one();
        if (parentMenu != null) {
            //为什么要查然后判空？因为万一你新增的是顶级菜单怎么办？会出现空指针
            sysUserRoleMapper.updateSysRoleMenuIsHalf(parentMenu.getId()); //将父菜单id为该id的关联表的记录都ishalf改成1
            //也可以写成
            //sysUserRoleMapper.update(new QueryWapper<SysRoleMenu>().set("is_half",1).eq("id",parentMenu.getId()));
            updateIsHalfById(parentMenu); //递归调用，因为菜单时分层的
        }
    }

    @Override
    public Result updateMenu(SysMenu sysMenu) {
        updateById(sysMenu);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result deleteMenu(Long id) {
        //如果没有子菜单才能删除
        LambdaQueryWrapper<SysMenu> childWrapper = new LambdaQueryWrapper<>();
        childWrapper.eq(SysMenu::getParentId, id)
                .eq(SysMenu::getIsDeleted, 0);
        List<SysMenu> childMenuList = this.list(childWrapper);
        //有的话抛出异常警告
        if (!childMenuList.isEmpty()) {
            return Result.build(null, ResultCodeEnum.NODE_ERROR);
        }
        update().set("is_deleted", 1).eq("id", id).update();
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result findMenuByUserId() {
        Long id = AuthContextUtil.get().getId();
        List<SysMenu> menusListBelongToMe = MenuHelper.buildTree(sysUserRoleMapper.findMenuByUserId(id));
        return Result.build(menusListBelongToMe, ResultCodeEnum.SUCCESS);
    }
}
