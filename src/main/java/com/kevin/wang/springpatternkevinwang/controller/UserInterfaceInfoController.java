package com.kevin.wang.springpatternkevinwang.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.kevin.common.model.entity.User;
import com.kevin.common.model.entity.UserInterfaceInfo;
import com.kevin.common.model.vo.UserInterfaceInfoVO;
import com.kevin.common.model.vo.UserVO;
import com.kevin.wang.springpatternkevinwang.annotation.AuthCheck;
import com.kevin.wang.springpatternkevinwang.common.BaseResponse;
import com.kevin.wang.springpatternkevinwang.common.DeleteRequest;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.common.ResultUtils;
import com.kevin.wang.springpatternkevinwang.constant.UserConstant;
import com.kevin.wang.springpatternkevinwang.exception.BussinessException;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.model.dto.common.IdRequest;
import com.kevin.wang.springpatternkevinwang.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.kevin.wang.springpatternkevinwang.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.kevin.wang.springpatternkevinwang.model.dto.userInterfaceInfo.UserInterfaceInfoUpadateRequest;
import com.kevin.wang.springpatternkevinwang.service.UserInterfaceInfoService;
import com.kevin.wang.springpatternkevinwang.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wang
 * @create 2023-2023-23-22:44
 */
@RestController
@Slf4j
@RequestMapping("/userInterfaceInfo")
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    @GetMapping("/list")
    @AuthCheck(mustRole = UserConstant.USER_ADMIN)
    public BaseResponse<Page<UserInterfaceInfoVO>> getUserInterfaceInfoList(UserInterfaceInfoQueryRequest queryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(queryRequest==null,new BussinessException(ErrorCode.PARAMS_ERROR));
        long pageSize = queryRequest.getPageSize();
        long current = queryRequest.getCurrent();
        User loginUser = userService.getLoginUser(request);
        // 不是管理员的话，就让他只能够管理属于自己的接口
        QueryWrapper<UserInterfaceInfo> queryWrapper = userInterfaceInfoService.getQueryWrapper(queryRequest);
        Page<UserInterfaceInfo> page = userInterfaceInfoService.page(new Page<>(current, pageSize), queryWrapper);
        Page<UserInterfaceInfoVO> pageVo = userInterfaceInfoService.getUserInterfaceInfoVOPage(page);
        return ResultUtils.success(pageVo);
    }

    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.USER_ADMIN)
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userInterfaceInfoRequest==null,ErrorCode.PARAMS_ERROR);

        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtil.copyProperties(userInterfaceInfoRequest,userInterfaceInfo);
        // 填充属性值
        User loginUser = userService.getLoginUser(request);
        userInterfaceInfo.setUserId(loginUser.getId());
        boolean save = userInterfaceInfoService.save(userInterfaceInfo);

        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);
        Long userInterfaceInfoId = userInterfaceInfo.getId();
        return ResultUtils.success(userInterfaceInfoId);
    }

    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.USER_ADMIN)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Boolean> deleteUserInterfaceInfo( DeleteRequest deleteRequest,HttpServletRequest request) {
        if(deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(deleteRequest.getId());
        ThrowUtils.throwIf(userInterfaceInfo==null,ErrorCode.NOT_FOUND_ERROR);
        // 只有管理员才可以删除
        if(userId!=userInterfaceInfo.getUserId() && !userService.isAdmin(request)) {
            throw new BussinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = userInterfaceInfoService.removeById(deleteRequest.getId());
        ThrowUtils.throwIf(!result,ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.USER_ADMIN)
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpadateRequest userInterfaceInfoRequest, HttpServletRequest request) {

        if(userInterfaceInfoRequest == null || userInterfaceInfoRequest.getId() <= 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setId(userInterfaceInfoRequest.getId())
                    .setStatus(userInterfaceInfoRequest.getStatus())
                    .setLeftNum(userInterfaceInfoRequest.getLeftNum())
                    .setTotalNum(userInterfaceInfoRequest.getTotalNum());
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo,false);
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(userInterfaceInfo.getId());
        ThrowUtils.throwIf(oldUserInterfaceInfo==null,ErrorCode.NOT_FOUND_ERROR);
        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/vo")
    public BaseResponse<UserInterfaceInfoVO> getUserInterfaceInfoVo(Long userInterfaceInfoId, HttpServletRequest request) {
        if(userInterfaceInfoId == null || userInterfaceInfoId <= 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(userInterfaceInfoId);
        ThrowUtils.throwIf(userInterfaceInfo==null,ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(userInterfaceInfoService.getUserInterfaceInfoVO(userInterfaceInfo));
    }

    @PostMapping("/listPageVo")
    public BaseResponse<Page<UserInterfaceInfoVO>> listUserInterfaceInfoVo(@RequestBody UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest, HttpServletRequest request) {
        long currentpage = userInterfaceInfoQueryRequest.getCurrent();
        long pagesize = userInterfaceInfoQueryRequest.getPageSize();

        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(currentpage, pagesize),userInterfaceInfoService.getQueryWrapper(userInterfaceInfoQueryRequest));

        return ResultUtils.success(userInterfaceInfoService.getUserInterfaceInfoVOPage(userInterfaceInfoPage));
    }

    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<UserInterfaceInfoVO>> listMyPageVo(@RequestBody UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userInterfaceInfoQueryRequest==null,ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser==null,ErrorCode.NOT_LOGIN_ERROR);
        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(userInterfaceInfoQueryRequest.getCurrent(), userInterfaceInfoQueryRequest.getPageSize()),
                userInterfaceInfoService.getQueryWrapper(userInterfaceInfoQueryRequest));
        return ResultUtils.success(userInterfaceInfoService.getUserInterfaceInfoVOPage(userInterfaceInfoPage));
    }

    @PostMapping("/edit")
    public BaseResponse<Boolean> editUserInterfaceInfo(@RequestBody UserInterfaceInfoUpadateRequest userInterfaceInfoUpadateRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userInterfaceInfoUpadateRequest ==null,ErrorCode.PARAMS_ERROR);

        Long userInterfaceInfoId = userInterfaceInfoUpadateRequest.getId();
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtil.copyProperties(userInterfaceInfoUpadateRequest,userInterfaceInfo);

        if(userInterfaceInfoId == null || userInterfaceInfoId <= 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser==null,ErrorCode.NOT_LOGIN_ERROR);
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(userInterfaceInfoId);
        Long userId = oldUserInterfaceInfo.getUserId();
        // 用户只能修改属于自己的接口
        if(!userId.equals(loginUser.getId())&& !userService.isAdmin(loginUser)){
            throw new BussinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<UserInterfaceInfoVO> getUserInterfaceInfo(IdRequest idRequest, HttpServletRequest request) {
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(idRequest.getId());
        UserInterfaceInfoVO userInterfaceInfoVO = userInterfaceInfoService.getUserInterfaceInfoVO(userInterfaceInfo);
        return ResultUtils.success(userInterfaceInfoVO);
    }


    @GetMapping("/getMaxInvoke")
    public BaseResponse<?> getMaxInvoke(HttpServletRequest request) {
        List<UserInterfaceInfoVO> userInterfaceInfoVOS = userInterfaceInfoService.slectMaxInvokeInterface();
        userInterfaceInfoVOS.stream().forEach(userInterfaceInfoVO -> {
            UserVO userVO = userService.getUserVO(userService.getById(userInterfaceInfoVO.getUserId()));
        });
        return ResultUtils.success(userInterfaceInfoVOS);
    }

    /**
     * A description of the entire Java function.
     *
     * @param  idRequest  description of the idRequest parameter
     * @param  request    description of the request parameter
     * @return            description of the return value
     */
    @GetMapping("/invoke")
    public BaseResponse<?> invoke(IdRequest idRequest,HttpServletRequest request) {
        ThrowUtils.throwIf(idRequest==null || request==null,ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser==null,ErrorCode.NOT_LOGIN_ERROR);
        Long userId = loginUser.getId();
        boolean result = userInterfaceInfoService.invokeAdd(idRequest.getId(), userId);
        return ResultUtils.success(result);
    }
}


