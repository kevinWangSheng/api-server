package com.kevin.wang.springpatternkevinwang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kevin.common.model.entity.UserInterfaceInfo;
import com.kevin.common.model.vo.UserInterfaceInfoVO;

import java.util.List;

/**
* @author wang sheng hui
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-09-01 22:31:51
* @Entity com.kevin.wang.springpatternkevinwang.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfoVO> slectMaxInvokeInterface();
}




