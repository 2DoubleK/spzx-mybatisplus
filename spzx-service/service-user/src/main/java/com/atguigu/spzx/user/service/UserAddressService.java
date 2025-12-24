package com.atguigu.spzx.user.service;

import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserAddressService extends IService<UserAddress> {
    Result findUserAddressList();
    Result saveRegion(UserAddress address);


}
