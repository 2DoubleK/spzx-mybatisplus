package com.atguigu.spzx.user.service.impl;

import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.service.user.client.service.UserAddressApiService;
import com.atguigu.spzx.user.mapper.UserAddressMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DubboService
public class UserAddressApiServiceImpl implements UserAddressApiService {
    //TODD
    //远程调用接口，返回用户默认收获地址
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public UserAddress getUserDefaultAddress(Long userId, Long userAddressId) {
        if (userId == null) {
            return null;
        }
        QueryWrapper<UserAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("is_deleted", 0);
        if (userAddressId != null) { // 查指定地址
            wrapper.eq("id", userAddressId);
        } else {// 查默认地址
            wrapper.eq("is_default", 1);
        }
        return userAddressMapper.selectOne(wrapper);
    }

}
