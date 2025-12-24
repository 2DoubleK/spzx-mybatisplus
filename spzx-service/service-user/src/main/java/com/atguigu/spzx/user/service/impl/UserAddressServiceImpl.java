package com.atguigu.spzx.user.service.impl;

import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.user.mapper.UserAddressMapper;
import com.atguigu.spzx.user.service.UserAddressService;
import com.atguigu.spzx.utils.AuthContextUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public Result findUserAddressList() {
        Long userId = AuthContextUtil.getUserInfo().getId();
        List<UserAddress> userAddresses = userAddressMapper.queryAllUserAddressById(userId);
        return Result.build(userAddresses, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result saveRegion(UserAddress address) {
        if (address != null) {
            address.setUserId(AuthContextUtil.getUserInfo().getId());
            save(address);
        }
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }


}
