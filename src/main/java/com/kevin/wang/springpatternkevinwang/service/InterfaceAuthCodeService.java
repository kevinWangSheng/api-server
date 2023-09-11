package com.kevin.wang.springpatternkevinwang.service;

import com.kevin.wang.springpatternkevinwang.model.entity.InterfaceAuthCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author wang sheng hui
* @description 针对表【interface_auth_code(接口授权信息)】的数据库操作Service
* @createDate 2023-09-10 16:25:26
*/
public interface InterfaceAuthCodeService extends IService<InterfaceAuthCode> {

    InterfaceAuthCode getByInterfaceInfoId(Long id);
}
