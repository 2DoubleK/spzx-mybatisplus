package com.atguigu.spzx.manager.Utils;

import com.atguigu.spzx.model.entity.system.SysMenu;
import org.springframework.expression.spel.ast.Literal;

import java.util.ArrayList;
import java.util.List;

public class MenuHelper {
    public static List<SysMenu> buildTree(List<SysMenu> sysMenusList) {
        // 判空：避免传入null导致的NPE
        if (sysMenusList == null || sysMenusList.isEmpty()) {
            return new ArrayList<>();
        }
        //1. 定义一个list集合接取返回的结果***
        List<SysMenu> tree = new ArrayList<>();
        //2.遍历集合，查看是否是自己的子节点
        for (SysMenu sysMenu : sysMenusList) {
            // 2. 安全转换：避免parentId为null的情况
            Long parentId = sysMenu.getParentId() == null ? 0L : sysMenu.getParentId().longValue();
            if (parentId == 0) {
                tree.add(findChild(sysMenu, sysMenusList));
            }
        }
        return tree;
    }

    public static SysMenu findChild(SysMenu father, List<SysMenu> sysMenuList) {
        // 避免重复初始化：只有children为null时才初始化
        if (father.getChildren() == null) {
            father.setChildren(new ArrayList<>());
        }
        //1. 遍历传入的集合
        for (SysMenu child : sysMenuList) {
            // 避免id/parentId为null
            Long fatherId = father.getId() == null ? 0L : father.getId().longValue();
            Long childParentId = child.getParentId() == null ? 0L : child.getParentId().longValue();
            //2.如果节点1id等与该节点2的父节点，则它是改节点的父节点
            if (fatherId == childParentId) {
                //递归调用，因为节点2也有自己的子节点以此类推
                father.getChildren().add(findChild(child, sysMenuList));
            }
        }
        return father;
    }
}
