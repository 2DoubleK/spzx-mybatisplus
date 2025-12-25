package com.atguigu.spzx.user.service.impl;

import com.atguigu.spzx.service.user.client.service.UserBrowseHistoryApiService;
import com.atguigu.spzx.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@DubboService
public class UserBrowseHistoryApiServiceImpl implements UserBrowseHistoryApiService {
    @Autowired
    private UserInfoService userInfoService;
    @Override
    public void addBrowseHistory(Long skuId,Long user_id) {
        userInfoService.addBrowseHistory(skuId,user_id);
    }
}
