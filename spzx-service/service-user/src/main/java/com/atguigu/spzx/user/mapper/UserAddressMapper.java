package com.atguigu.spzx.user.mapper;

import com.atguigu.spzx.model.entity.user.UserAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {
    List<UserAddress> queryAllUserAddressById(Long userId);

    void saveAdd(UserAddress address);
}
