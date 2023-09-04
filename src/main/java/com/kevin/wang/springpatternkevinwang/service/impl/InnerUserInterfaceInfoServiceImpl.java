package com.kevin.wang.springpatternkevinwang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kevin.common.model.entity.InterfaceInfo;
import com.kevin.common.model.entity.User;
import com.kevin.common.service.InnerUserInterfaceInfoService;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.service.InterfaceInfoService;
import com.kevin.wang.springpatternkevinwang.service.UserInterfaceInfoService;
import com.kevin.wang.springpatternkevinwang.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author wang
 * @create 2023-2023-03-22:51
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Override
    public boolean invokeCount(long userInterfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeAdd(userInterfaceInfoId,userId);
    }

    @Override
    public User getInvokeUser(String accessKey, String secretKey) {
        ThrowUtils.throwIf(accessKey==null||secretKey==null, ErrorCode.PARAMS_ERROR);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("accessKey",accessKey).eq("sercetKey",secretKey);
        User invokeUser = userService.getOne(userQueryWrapper);
        return invokeUser;
    }

    @Override
    public InterfaceInfo getInterface(String path, String method) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(path,method), ErrorCode.PARAMS_ERROR);
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("path",path).eq("method",method);
        InterfaceInfo interfaceInfo = interfaceInfoService.getOne(wrapper);
        return interfaceInfo;
    }
}
