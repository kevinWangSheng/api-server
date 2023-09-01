package com.kevin.wang.springpatternkevinwang.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.exception.BussinessException;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.kevin.wang.springpatternkevinwang.model.entity.InterfaceInfo;
import com.kevin.wang.springpatternkevinwang.model.entity.User;
import com.kevin.wang.springpatternkevinwang.model.vo.InterfaceInfoVO;
import com.kevin.wang.springpatternkevinwang.service.InterfaceInfoService;
import com.kevin.wang.springpatternkevinwang.mapper.InterfaceInfoMapper;
import com.kevin.wang.springpatternkevinwang.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author wang sheng hui
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2023-08-27 13:17:48
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;
    @Resource
    private UserService userService;
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {

        ThrowUtils.throwIf(interfaceInfo==null, ErrorCode.PARAMS_ERROR);
        String name = interfaceInfo.getName();
        String url = interfaceInfo.getUrl();
        String method = interfaceInfo.getMethod();
        Long userId = interfaceInfo.getUserId();


        // 如果任何这三个字段为空，那么就抛出异常
        ThrowUtils.throwIf(StringUtils.isAllBlank(name,url,method)|| userId==null,ErrorCode.PARAMS_ERROR);

        if(name.length()>100){
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"标题过长");
        }

        if(url!=null && url.length()>1000){
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"url长度过长");
        }
        // 验证当前用户是否登录，如果没有登录，不能够对他进行操作.
        Optional.of(userService.getById(userId)).orElseThrow(()->new BussinessException(ErrorCode.NOT_LOGIN_ERROR));
    }

    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo) {
        if(interfaceInfo==null){
            return null;
        }
        InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        BeanUtil.copyProperties(interfaceInfo,interfaceInfoVO );
        return interfaceInfoVO;
    }

    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage) {
        ThrowUtils.throwIf(interfaceInfoPage==null,ErrorCode.PARAMS_ERROR);

        List<InterfaceInfo> infoPageRecords = interfaceInfoPage.getRecords();

        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getPages());

        List<InterfaceInfoVO> interfaceInfoVOList = new ArrayList<>();
        infoPageRecords.stream().forEach(interfaceInfo -> {
            Long userId = interfaceInfo.getUserId();
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            if(userId!=null){
                User user = userService.getById(userId);
                interfaceInfoVO.setUserVO(userService.getUserVO(user));
                BeanUtil.copyProperties(interfaceInfo,interfaceInfoVO);
                interfaceInfoVOList.add(interfaceInfoVO);
            }
        });

        interfaceInfoVOPage.setRecords(interfaceInfoVOList);
        return interfaceInfoVOPage;
    }

    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        ThrowUtils.throwIf(interfaceInfoQueryRequest==null, ErrorCode.PARAMS_ERROR);

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(interfaceInfoQueryRequest.getId()!=null,InterfaceInfo::getId,interfaceInfoQueryRequest.getId())
                .eq(interfaceInfoQueryRequest.getMethod()!=null,InterfaceInfo::getMethod,interfaceInfoQueryRequest.getMethod())
                .like(interfaceInfoQueryRequest.getUrl()!=null,InterfaceInfo::getUrl,interfaceInfoQueryRequest.getUrl())
                .eq(interfaceInfoQueryRequest.getUserId()!=null,InterfaceInfo::getUserId,interfaceInfoQueryRequest.getUserId())
                .like(interfaceInfoQueryRequest.getDescription()!=null,InterfaceInfo::getDescription,interfaceInfoQueryRequest.getDescription())
                .like(interfaceInfoQueryRequest.getName()!=null,InterfaceInfo::getName,interfaceInfoQueryRequest.getName())
                .like(interfaceInfoQueryRequest.getRequestHeader()!=null,InterfaceInfo::getRequestHeader,interfaceInfoQueryRequest.getRequestHeader())
                .like(interfaceInfoQueryRequest.getResponseHeader()!=null,InterfaceInfo::getResponseHeader,interfaceInfoQueryRequest.getResponseHeader())
                .eq(interfaceInfoQueryRequest.getStatus()!=null,InterfaceInfo::getStatus,interfaceInfoQueryRequest.getStatus());
        return queryWrapper;
    }

    @Override
    public List<InterfaceInfoVO> slectMaxInvokeInterface() {
        return interfaceInfoMapper.getMaxInvokeInterface();
    }
}




