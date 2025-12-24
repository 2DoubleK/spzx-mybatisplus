package com.atguigu.spzx.service.user.client.service;

import com.atguigu.spzx.model.entity.user.UserAddress;

public interface UserAddressApiService {
    UserAddress getUserDefaultAddress(Long userId,Long userAddressId);
}
