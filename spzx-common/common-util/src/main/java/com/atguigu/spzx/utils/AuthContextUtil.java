package com.atguigu.spzx.utils;


import com.atguigu.spzx.model.entity.system.SysUser;

public class AuthContextUtil {

    //创建threadlocal对象
    private static final ThreadLocal<SysUser> threadLocal = new ThreadLocal<>();

    //撰写threadLocal的get/set/remove方法
    public static SysUser get() {
        return threadLocal.get();
    }

    public static void set(SysUser sysUser) {
        threadLocal.set(sysUser);
    }

    public static void remove() {
        threadLocal.remove();
    }
}
