package com.kevin.wang.springpatternkevinwang.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dubbo.model.entity.InterfaceInfo;
import com.dubbo.model.entity.UserInterfaceInfo;
import com.dubbo.service.InnerInterfaceInfoService;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.service.InterfaceInfoService;
import com.kevin.wang.springpatternkevinwang.service.UserInterfaceInfoService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author wang
 * @create 2023-2023-03-22:49
 */
@DubboService
public class InnerInterfaceInfoImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        interfaceInfoService.validInterfaceInfo(interfaceInfo,add);
    }

    @Override
    public InterfaceInfo getInterface(String path, String method) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(path,method), ErrorCode.PARAMS_ERROR);
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("url",path).eq("method",method);
        InterfaceInfo interfaceInfo = interfaceInfoService.getOne(wrapper);
        return interfaceInfo;
    }
}
