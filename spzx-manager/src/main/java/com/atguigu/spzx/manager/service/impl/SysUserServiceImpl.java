package com.atguigu.spzx.manager.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.exception.GuiguException;
import com.atguigu.spzx.manager.mapper.SysRoleMapper;
import com.atguigu.spzx.manager.mapper.SysUserMapper;
import com.atguigu.spzx.manager.mapper.SysUserRoleMapper;
import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.atguigu.spzx.manager.Utils.Constant.USER_LOGIN_TOKEN;
import static com.atguigu.spzx.manager.Utils.Constant.USER_VALIDATE_CODE;
import static com.atguigu.spzx.utils.MD5Utils.MD5Encrypted;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public LoginVo login(LoginDto loginDto) {
        //获取输入的验证码
        String captcha = loginDto.getCaptcha();
        String codeKey = loginDto.getCodeKey();
        //比对是否与redis的验证码一致
        String redisCode = redisTemplate.opsForValue().get(USER_VALIDATE_CODE + codeKey);
        //不一致登录失败，返回错误信息
        if (StrUtil.isEmpty(redisCode) || !StrUtil.equalsAnyIgnoreCase(redisCode, captcha)) {
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        //一致，删除redis里的验证码，进入下一步流程
        redisTemplate.delete(USER_VALIDATE_CODE + codeKey);
        //获取提交的用户名
        String username = loginDto.getUserName();
        //更具用户名查询sys_user表
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
        //没有查询到用户
        if (sysUser == null) {
            //返回错误信息
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }
        //查询到用户
        //比对密码是否一致
        String database_password = sysUser.getPassword();
        String input_password = loginDto.getPassword();
        input_password = MD5Encrypted(input_password);
        //密码不一致
        if (!input_password.equals(database_password)) {
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }
        //密码一致，生成token返回
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set(USER_LOGIN_TOKEN + token,
                JSON.toJSONString(sysUser),
                7,
                TimeUnit.DAYS
        );
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token); //将token返回给前端用于存储
        return loginVo;
    }

    @Override
    public SysUser getUserInfo(String token) {
        String userJson = redisTemplate.opsForValue().get(USER_LOGIN_TOKEN + token);
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public void logout(String token) {
        redisTemplate.delete(USER_LOGIN_TOKEN + token);
    }

    @Override
    public Result<?> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> lq = new LambdaQueryWrapper<>();
        if (sysUserDto != null && StrUtil.isNotBlank(sysUserDto.getKeyword())) {
            lq.like(SysUser::getUserName, sysUserDto.getKeyword());
        }
        if (sysUserDto.getCreateTimeBegin() != null && sysUserDto.getCreateTimeBegin() != "") {
            lq.ge(SysUser::getCreateTime, sysUserDto.getCreateTimeBegin());
        }
        if (sysUserDto.getCreateTimeBegin() != null && sysUserDto.getCreateTimeEnd() != "") {
            lq.le(SysUser::getCreateTime, sysUserDto.getCreateTimeEnd());
        }
        lq.ne(SysUser::getIsDeleted, 1).orderByDesc(SysUser::getCreateTime);
        Page<SysUser> userPage = this.page(page, lq);
        return Result.build(userPage, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result<?> saveSysUser(SysUser sysUser) {
        //根据输入用户名查询用户
        if (sysUser.getId() != null) { //是更新用户
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
        //对密码进行加密
        String password = sysUser.getPassword();
        String digestPassword =MD5Encrypted(password); //该方法接收的是byte
        sysUser.setPassword(digestPassword);
        sysUser.setStatus(0);
        save(sysUser);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> updateSysUser(SysUser sysUser) {
        // 1. 校验用户是否存在
        SysUser existUser = this.getById(sysUser.getId());
        if (existUser == null) {
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_NOT_EXISTS);
        }

        // 2. 构建动态更新条件，与XML中的update语句逻辑完全一致
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        // 仅当字段非空时，才添加更新条件
        updateWrapper
                .set(StrUtil.isNotBlank(sysUser.getUserName()), SysUser::getUserName, sysUser.getUserName())
                .set(StrUtil.isNotBlank(sysUser.getPassword()), SysUser::getPassword, sysUser.getPassword())
                .set(StrUtil.isNotBlank(sysUser.getName()), SysUser::getName, sysUser.getName())
                .set(StrUtil.isNotBlank(sysUser.getPhone()), SysUser::getPhone, sysUser.getPhone())
                .set(StrUtil.isNotBlank(sysUser.getDescription()), SysUser::getDescription, sysUser.getDescription())
                .set(sysUser.getStatus() != null, SysUser::getStatus, sysUser.getStatus())
                .set(true, SysUser::getUpdateTime, new Date()) // 强制设置更新时间为当前时间
                .set(StrUtil.isNotBlank(sysUser.getAvatar()), SysUser::getAvatar, sysUser.getAvatar())
                .eq(SysUser::getId, sysUser.getId()); // where id = #{id}

        // 3. 执行更新操作
        boolean updateSuccess = this.update(updateWrapper);
        if (!updateSuccess) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }

        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result<?> deleteSysUser(Long id) {
        update().eq("id", id).set("is_deleted", 1).update();
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }


    @Override
    public Result<?> assignSysRole(AssginRoleDto assginRoleDto) {
        //根据用户删除原来分配的角色
        sysUserRoleMapper.deleteByUserId(assginRoleDto.getUserId());
        //重新为用户分配角色
        List<Long> roleIdList = assginRoleDto.getRoleIdList();  //一个角色可以被分配多个角色
        for (Long roleId : roleIdList) {
            sysUserRoleMapper.doAssign(assginRoleDto.getUserId(), roleId);
        }
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result<?> queryAllSysRole() {

        //1.获取所有角色和以及分配的角色
        List<Map<String, Object>> map = sysRoleMapper.queryAllRole();
        //2.查询以及分配过的角色列表
        return Result.build(map, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result<?> querySysRoleByUserId(Long id) {
        //只需要比对当前用户的roleIdList里有无相同
        List<Long> roleIdList = sysUserRoleMapper.querySysRoleByUserId(id);
        return Result.build(roleIdList, ResultCodeEnum.SUCCESS);
    }
}
