package com.kevin.wang.springpatternkevinwang.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.kevin.wang.springpatternkevinwang.annotation.AuthCheck;
import com.kevin.wang.springpatternkevinwang.common.BaseResponse;
import com.kevin.wang.springpatternkevinwang.common.DeleteRequest;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.common.ResultUtils;
import com.kevin.wang.springpatternkevinwang.constant.UserConstant;
import com.kevin.wang.springpatternkevinwang.exception.BussinessException;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.model.dto.common.IdRequest;
import com.kevin.wang.springpatternkevinwang.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.kevin.wang.springpatternkevinwang.model.dto.interfaceInfo.InterfaceInfoEditRequest;
import com.kevin.wang.springpatternkevinwang.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.kevin.wang.springpatternkevinwang.model.dto.interfaceInfo.InterfaceInvokeRequest;
import com.kevin.wang.springpatternkevinwang.model.entity.InterfaceInfo;
import com.kevin.wang.springpatternkevinwang.model.entity.User;
import com.kevin.wang.springpatternkevinwang.model.enums.InterfaceInfoStatusEnum;
import com.kevin.wang.springpatternkevinwang.model.enums.UserRoleEnums;
import com.kevin.wang.springpatternkevinwang.model.vo.InterfaceInfoVO;
import com.kevin.wang.springpatternkevinwang.model.vo.UserVO;
import com.kevin.wang.springpatternkevinwang.service.InterfaceInfoService;
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
@RequestMapping("/interfaceInfo")
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfoVO>> getInterfaceInfoList(InterfaceInfoQueryRequest queryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(queryRequest==null,new BussinessException(ErrorCode.PARAMS_ERROR));
        long pageSize = queryRequest.getPageSize();
        long current = queryRequest.getCurrent();
        User loginUser = userService.getLoginUser(request);
        // 不是管理员的话，就让他只能够管理属于自己的接口
        if(loginUser.getUserRole()!= UserRoleEnums.ADMIN.getValue()){
            queryRequest.setUserId(loginUser.getId());
        }
        QueryWrapper<InterfaceInfo> queryWrapper = interfaceInfoService.getQueryWrapper(queryRequest);
        Page<InterfaceInfo> page = interfaceInfoService.page(new Page<>(current, pageSize), queryWrapper);
        Page<InterfaceInfoVO> pageVo = interfaceInfoService.getInterfaceInfoVOPage(page);
        return ResultUtils.success(pageVo);
    }

    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(interfaceInfoRequest==null,ErrorCode.PARAMS_ERROR);
        String name = interfaceInfoRequest.getName();
        String description = interfaceInfoRequest.getDescription();
        String url = interfaceInfoRequest.getUrl();
        String method = interfaceInfoRequest.getMethod();
        String requestHeader = interfaceInfoRequest.getRequestHeader();
        String responseHeader = interfaceInfoRequest.getResponseHeader();

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        // 填充属性值
        interfaceInfo.setName(name).setDescription(description).setUrl(url).setMethod(method).setRequestHeader(requestHeader).setResponseHeader(responseHeader);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean save = interfaceInfoService.save(interfaceInfo);

        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);
        Long interfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(interfaceInfoId);
    }

    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Boolean> deleteInterfaceInfo( DeleteRequest deleteRequest,HttpServletRequest request) {
        if(deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        InterfaceInfo interfaceInfo = interfaceInfoService.getById(deleteRequest.getId());
        ThrowUtils.throwIf(interfaceInfo==null,ErrorCode.NOT_FOUND_ERROR);
        // 只有管理员才可以删除
        if(userId!=interfaceInfo.getUserId() && !userService.isAdmin(request)) {
            throw new BussinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.removeById(deleteRequest.getId());
        ThrowUtils.throwIf(!result,ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.USER_ADMIN)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoEditRequest interfaceInfoRequest, HttpServletRequest request) {

        if(interfaceInfoRequest == null || interfaceInfoRequest.getId() <= 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(interfaceInfoRequest.getId())
                    .setName(interfaceInfoRequest.getName())
                    .setDescription(interfaceInfoRequest.getDescription())
                    .setUrl(interfaceInfoRequest.getUrl())
                    .setMethod(interfaceInfoRequest.getMethod())
                .setResponseHeader(interfaceInfoRequest.getRequestHeader())
                .setResponseHeader(interfaceInfoRequest.getResponseHeader())
                .setStatus(interfaceInfoRequest.getStatus())
                .setUserId(interfaceInfoRequest.getUserId());

        interfaceInfoService.validInterfaceInfo(interfaceInfo,false);
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(interfaceInfo.getId());
        ThrowUtils.throwIf(oldInterfaceInfo==null,ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/vo")
    public BaseResponse<InterfaceInfoVO> getInterfaceInfoVo(Long interfaceInfoId, HttpServletRequest request) {
        if(interfaceInfoId == null || interfaceInfoId <= 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        ThrowUtils.throwIf(interfaceInfo==null,ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVO(interfaceInfo));
    }

    @PostMapping("/listPageVo")
    public BaseResponse<Page<InterfaceInfoVO>> listInterfaceInfoVo(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        long currentpage = interfaceInfoQueryRequest.getCurrent();
        long pagesize = interfaceInfoQueryRequest.getPageSize();

        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(currentpage, pagesize),interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));

        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage));
    }

    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> listMyPageVo(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(interfaceInfoQueryRequest==null,ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser==null,ErrorCode.NOT_LOGIN_ERROR);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(interfaceInfoQueryRequest.getCurrent(), interfaceInfoQueryRequest.getPageSize()),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage));
    }

    @PostMapping("/edit")
    public BaseResponse<Boolean> editInterfaceInfo(@RequestBody InterfaceInfoEditRequest interfaceInfoEditRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(interfaceInfoEditRequest==null,ErrorCode.PARAMS_ERROR);

        Long interfaceInfoId = interfaceInfoEditRequest.getId();
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtil.copyProperties(interfaceInfoEditRequest,interfaceInfo);

        if(interfaceInfoId == null || interfaceInfoId <= 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser==null,ErrorCode.NOT_LOGIN_ERROR);
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        Long userId = oldInterfaceInfo.getUserId();
        // 用户只能修改属于自己的接口
        if(!userId.equals(loginUser.getId())&& !userService.isAdmin(loginUser)){
            throw new BussinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    @PostMapping("/online")
    public BaseResponse<Boolean> letInterfaceOnline(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(idRequest==null,ErrorCode.PARAMS_ERROR);
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(idRequest.getId());
        ThrowUtils.throwIf(interfaceInfo==null,ErrorCode.NOT_FOUND_ERROR);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.Online.getValue());
        InterfaceInfo newInterface = new InterfaceInfo();
        newInterface.setId(interfaceInfo.getId()).setStatus(InterfaceInfoStatusEnum.Online.getValue());
        boolean result = interfaceInfoService.updateById(newInterface);
        return ResultUtils.success(result);
    }

    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.USER_ADMIN)
    public BaseResponse<Boolean> letInterfaceDown(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(idRequest==null,ErrorCode.PARAMS_ERROR);
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(idRequest.getId());
        ThrowUtils.throwIf(interfaceInfo==null,ErrorCode.NOT_FOUND_ERROR);
        InterfaceInfo newInterface = new InterfaceInfo();
        newInterface.setId(interfaceInfo.getId()).setStatus(InterfaceInfoStatusEnum.Down.getValue());
        boolean result = interfaceInfoService.updateById(newInterface);
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<InterfaceInfoVO> getInterfaceInfo(IdRequest idRequest, HttpServletRequest request) {
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(idRequest.getId());
        InterfaceInfoVO interfaceInfoVO = interfaceInfoService.getInterfaceInfoVO(interfaceInfo);
        return ResultUtils.success(interfaceInfoVO);
    }

    @PostMapping("/invoke")
    public BaseResponse<?> invokeInterface(@RequestBody InterfaceInvokeRequest invokeRequest,HttpServletRequest request){
        ThrowUtils.throwIf(invokeRequest==null,ErrorCode.PARAMS_ERROR);
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(invokeRequest.getId());
        ThrowUtils.throwIf(interfaceInfo==null,ErrorCode.NOT_FOUND_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser==null,ErrorCode.NOT_LOGIN_ERROR);
        ThrowUtils.throwIf(interfaceInfo.getStatus()==InterfaceInfoStatusEnum.Down.getValue(),ErrorCode.OPERATION_ERROR);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();

        //这里还没有处理完成，想办法处理一下，如何根据这个accessKey进行对应的调用接口，
        //用什么方式来调用接口。

        return ResultUtils.success("ok");
    }

    @GetMapping("/getMaxInvoke")
    public BaseResponse<?> getMaxInvoke(HttpServletRequest request) {
        List<InterfaceInfoVO> interfaceInfoVOS = interfaceInfoService.slectMaxInvokeInterface();
        interfaceInfoVOS.stream().forEach(interfaceInfoVO -> {
            UserVO userVO = userService.getUserVO(userService.getById(interfaceInfoVO.getUserId()));
            interfaceInfoVO.setUserVO(userVO);
        });
        return ResultUtils.success(interfaceInfoVOS);
    }
}


