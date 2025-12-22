package com.atguigu.spzx.utils;


import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.entity.user.UserInfo;

public class AuthContextUtil {

    //创建threadlocal对象
    private static final ThreadLocal<SysUser> threadLocal = new ThreadLocal<>();
    private static final ThreadLocal<UserInfo> userInfoThreadLocal = new ThreadLocal<>();

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
    public static UserInfo getUserInfo() {
        return userInfoThreadLocal.get();
    }

    public static void setUserInfo(UserInfo userInfo) {
        userInfoThreadLocal.set(userInfo);
    }

    public static void removeUserInfo() {
        userInfoThreadLocal.remove();
    }
}
