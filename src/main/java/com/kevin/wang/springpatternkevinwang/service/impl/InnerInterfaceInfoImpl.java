package com.kevin.wang.springpatternkevinwang.service.impl;

import com.kevin.common.model.entity.InterfaceInfo;
import com.kevin.common.service.InnerInterfaceInfoService;
import com.kevin.wang.springpatternkevinwang.service.InterfaceInfoService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author wang
 * @create 2023-2023-03-22:49
 */
@DubboService
public class InnerInterfaceInfoImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        interfaceInfoService.validInterfaceInfo(interfaceInfo,add);
    }
}
