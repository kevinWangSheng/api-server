package com.kevin.wang.springpatternkevinwang.mapper;

import com.kevin.wang.springpatternkevinwang.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kevin.wang.springpatternkevinwang.model.vo.InterfaceInfoVO;

import java.util.List;

/**
* @author wang sheng hui
* @description 针对表【interface_info(接口信息)】的数据库操作Mapper
* @createDate 2023-08-27 13:17:48
* @Entity com.kevin.wang.springpatternkevinwang.model.entity.InterfaceInfo
*/
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

    List<InterfaceInfoVO> getMaxInvokeInterface();
}




