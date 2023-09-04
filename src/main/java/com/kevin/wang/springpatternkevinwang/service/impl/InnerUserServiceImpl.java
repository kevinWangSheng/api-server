package com.kevin.wang.springpatternkevinwang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevin.common.model.entity.User;
import com.kevin.common.service.InnerUserService;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.service.UserService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author wang
 * @create 2023-2023-03-22:41
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService{

    @Resource
    private UserService userService;
    @Override
    public User getInvokeUser(String accessKey) {
        ThrowUtils.throwIf(accessKey==null, ErrorCode.PARAMS_ERROR);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("accessKey",accessKey);
        User invokeUser = userService.getOne(wrapper);
        return invokeUser;
    }
}
