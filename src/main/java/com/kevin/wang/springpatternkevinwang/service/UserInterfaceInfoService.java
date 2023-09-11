package com.kevin.wang.springpatternkevinwang.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dubbo.model.entity.UserInterfaceInfo;
import com.kevin.wang.springpatternkevinwang.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kevin.wang.springpatternkevinwang.model.vo.UserInterfaceInfoVO;

import java.util.List;

/**
* @author wang sheng hui
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-09-01 22:31:51
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest queryRequest);

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b);

    Page<UserInterfaceInfoVO> getUserInterfaceInfoVOPage(Page<UserInterfaceInfo> page);

    UserInterfaceInfoVO getUserInterfaceInfoVO(UserInterfaceInfo userInterfaceInfo);

    List<UserInterfaceInfoVO> slectMaxInvokeInterface();

    boolean invokeAdd(long userInterfaceInfoId,long userId,Integer version);

    boolean invokeAdd(long userInterfaceInfoId,long userId);

    Page<String> getTopInterfaceInvoke(Integer pageSize);
}
