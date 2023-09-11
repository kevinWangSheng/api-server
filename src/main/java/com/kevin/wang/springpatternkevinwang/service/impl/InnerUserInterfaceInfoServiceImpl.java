package com.kevin.wang.springpatternkevinwang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dubbo.model.entity.InterfaceInfo;
import com.dubbo.model.entity.User;
import com.dubbo.model.entity.UserInterfaceInfo;
import com.dubbo.service.InnerUserInterfaceInfoService;
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
    public boolean invokeCount(long userInterfaceInfoId, long userId,Integer version) {
        return userInterfaceInfoService.invokeAdd(userInterfaceInfoId,userId,version);
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
    public UserInterfaceInfo canInvoke(long interfaceInfoId, long userId) {
        QueryWrapper<UserInterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("interfaceInfoId",interfaceInfoId).eq("userId",userId);
        wrapper.select("leftNum","version");
        UserInterfaceInfo userInterfaceInfoServiceOne = userInterfaceInfoService.getOne(wrapper);
        return userInterfaceInfoServiceOne;
    }
}
