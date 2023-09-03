package com.kevin.wang.springpatternkevinwang.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.exception.BussinessException;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.kevin.wang.springpatternkevinwang.model.entity.InterfaceInfo;
import com.kevin.wang.springpatternkevinwang.model.entity.Post;
import com.kevin.wang.springpatternkevinwang.model.entity.UserInterfaceInfo;
import com.kevin.wang.springpatternkevinwang.model.vo.UserInterfaceInfoVO;
import com.kevin.wang.springpatternkevinwang.service.UserInterfaceInfoService;
import com.kevin.wang.springpatternkevinwang.mapper.UserInterfaceInfoMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author wang sheng hui
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-09-01 22:31:51
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;
    @Override
    public QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        ThrowUtils.throwIf(userInterfaceInfoQueryRequest==null, ErrorCode.PARAMS_ERROR);

        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(userInterfaceInfoQueryRequest.getId()!=null,UserInterfaceInfo::getId,userInterfaceInfoQueryRequest.getId())
                .eq(userInterfaceInfoQueryRequest.getInterfaceInfoId()!=null,UserInterfaceInfo::getInterfaceInfoId,userInterfaceInfoQueryRequest.getInterfaceInfoId())
                .ge(userInterfaceInfoQueryRequest.getLeftNum()!=null,UserInterfaceInfo::getLeftNum,userInterfaceInfoQueryRequest.getLeftNum())
                .eq(userInterfaceInfoQueryRequest.getTotalNum()!=null,UserInterfaceInfo::getTotalNum,userInterfaceInfoQueryRequest.getTotalNum())
                .eq(userInterfaceInfoQueryRequest.getStatus()!=null,UserInterfaceInfo::getStatus,userInterfaceInfoQueryRequest.getStatus());
        return queryWrapper;
    }

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b) {

        ThrowUtils.throwIf(userInterfaceInfo==null, ErrorCode.PARAMS_ERROR);


        if(userInterfaceInfo.getLeftNum()<0){
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"接口调用剩余次数错误");
        }

        if(userInterfaceInfo.getTotalNum()<0 ){
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"接口总体调用次数出错");
        }
    }

    @Override
    public Page<UserInterfaceInfoVO> getUserInterfaceInfoVOPage(Page<UserInterfaceInfo> page) {
        ThrowUtils.throwIf(page==null, ErrorCode.PARAMS_ERROR);
        List<UserInterfaceInfo> records = page.getRecords();
        Page<UserInterfaceInfoVO> userInterfaceInfoVOPage = new Page<>(page.getCurrent(),page.getSize());
        List<UserInterfaceInfoVO> userInterfaceInfoVOList = userInterfaceInfoVOPage.getRecords();
        for(UserInterfaceInfo userInterfaceInfo:records){
            UserInterfaceInfoVO userInterfaceInfoVO = new UserInterfaceInfoVO();
            BeanUtil.copyProperties(userInterfaceInfo,userInterfaceInfoVO);
            userInterfaceInfoVOList.add(userInterfaceInfoVO);
        }
        userInterfaceInfoVOPage.setRecords(userInterfaceInfoVOList);
        return userInterfaceInfoVOPage;
    }

    @Override
    public UserInterfaceInfoVO getUserInterfaceInfoVO(UserInterfaceInfo userInterfaceInfo) {
        ThrowUtils.throwIf(userInterfaceInfo==null, ErrorCode.PARAMS_ERROR);
        UserInterfaceInfoVO userInterfaceInfoVO = new UserInterfaceInfoVO();
        BeanUtil.copyProperties(userInterfaceInfo,userInterfaceInfoVO);
        return userInterfaceInfoVO;
    }

    @Override
    public List<UserInterfaceInfoVO> slectMaxInvokeInterface() {
        return userInterfaceInfoMapper.slectMaxInvokeInterface();
    }

    @Override
    public boolean invokeAdd(long userInterfaceInfoId,long userId) {
        ThrowUtils.throwIf(userInterfaceInfoId<=0 || userId<=0, ErrorCode.PARAMS_ERROR);
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(UserInterfaceInfo::getId,userInterfaceInfoId)
                .eq(UserInterfaceInfo::getUserId,userId)
                .setSql("leftNum = leftNum - 1,totalNum = totalNum + 1");
        return update(updateWrapper);
    }
}




