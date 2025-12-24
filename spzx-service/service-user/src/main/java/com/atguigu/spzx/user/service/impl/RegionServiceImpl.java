package com.atguigu.spzx.user.service.impl;

import com.atguigu.spzx.model.entity.base.Region;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.user.mapper.RegionMapper;
import com.atguigu.spzx.user.service.RegionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    @Autowired
    private RegionMapper regionMapper;
    @Override
    public Result findByParentCode(Long parentCode) {
        //根据ParentCode获取，地区信息,默认是0
        List<Region> regions = query().eq("is_deleted", 0).eq("parent_code",parentCode).list();
        //List<Region> build3GradeTree(regions)
        return Result.build(regions, ResultCodeEnum.SUCCESS);
    }
}
