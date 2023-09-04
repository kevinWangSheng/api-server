package com.kevin.wang.springpatternkevinwang.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kevin.common.model.entity.InterfaceInfo;
import com.kevin.common.model.vo.InterfaceInfoVO;
import com.kevin.wang.springpatternkevinwang.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author wang sheng hui
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-08-27 13:17:48
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo post, boolean add);

    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo);

    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage);

    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    List<InterfaceInfoVO> slectMaxInvokeInterface();
}
