package com.kevin.wang.springpatternkevinwang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.exception.BussinessException;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.model.entity.InterfaceAuthCode;
import com.kevin.wang.springpatternkevinwang.service.InterfaceAuthCodeService;
import com.kevin.wang.springpatternkevinwang.mapper.InterfaceAuthCodeMapper;
import com.kevin.wang.springpatternkevinwang.service.InterfaceInfoService;
import org.springframework.stereotype.Service;

/**
* @author wang sheng hui
* @description 针对表【interface_auth_code(接口授权信息)】的数据库操作Service实现
* @createDate 2023-09-10 16:25:26
*/
@Service
public class InterfaceAuthCodeServiceImpl extends ServiceImpl<InterfaceAuthCodeMapper, InterfaceAuthCode>
    implements InterfaceAuthCodeService{

    @Override
    public InterfaceAuthCode getByInterfaceInfoId(Long id) {
        ThrowUtils.throwIf(id==null||id==0l,new BussinessException(ErrorCode.OPERATION_ERROR));
        InterfaceAuthCode interfaceAuthCode = new InterfaceAuthCode();
        interfaceAuthCode.setInterfaceInfoId(id);
        QueryWrapper<InterfaceAuthCode> queryWrapper = initQueryWrapper(interfaceAuthCode);
        return getOne(queryWrapper);
    }
    public QueryWrapper<InterfaceAuthCode> initQueryWrapper(InterfaceAuthCode interfaceAuthCode){
        QueryWrapper<InterfaceAuthCode> wrapper = new QueryWrapper<>();
        wrapper.eq(interfaceAuthCode.getInterfaceInfoId()!=null, "interface_info_id",interfaceAuthCode.getInterfaceInfoId())
                .eq(interfaceAuthCode.getAppCode()!=null,"app_code",interfaceAuthCode.getAppCode())
                .eq(interfaceAuthCode.getAppSecret()!=null,"app_secret",interfaceAuthCode.getAppSecret())
                .eq(interfaceAuthCode.getAppKey()!=null,"app_key",interfaceAuthCode.getAppKey())
                .eq("is_deleted","0");
        return wrapper;
    }
}




