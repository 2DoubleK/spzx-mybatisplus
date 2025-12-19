package com.atguigu.spzx.manager.Utils;

import com.atguigu.spzx.utils.AuthContextUtil;

public enum UploadAddr {
    USER_IMG("user_img/"+AuthContextUtil.get().getId().toString()),
    BRAND_IMG("brand"),
    UNKNOWTYPE("unKnowType");
    //等等
    ;

    private String prefix;

    UploadAddr(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
