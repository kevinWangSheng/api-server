package com.kevin.wang.springpatternkevinwang.service.impl;

import com.dubbo.model.dto.InvokeKeyDto;
import com.dubbo.service.ParamVerify;
import com.kevin.wang.springpatternkevinwang.utils.ParamUtils;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;


/**
 * @author wang
 * @create 2023-2023-09-11:50
 */
@DubboService
public class ParamVerifyServiceImpl implements ParamVerify {
    @Resource
    private ParamUtils paramUtils;
    @Override
    public boolean isVaild(InvokeKeyDto invokeKeyDto) {
        return paramUtils.vilateKeyParam(invokeKeyDto);
    }
}
