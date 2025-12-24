package com.atguigu.spzx.user.service;

import com.atguigu.spzx.model.entity.base.Region;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface RegionService extends IService<Region> {
    Result findByParentCode(Long parentCode);


}
