package com.kevin.wang.springpatternkevinwang.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dubbo.model.entity.User;
import com.kevin.wang.springpatternkevinwang.annotation.AuthCheck;
import com.kevin.wang.springpatternkevinwang.auth.AuthModel;
import com.kevin.wang.springpatternkevinwang.common.BaseResponse;
import com.kevin.wang.springpatternkevinwang.common.DeleteRequest;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.common.ResultUtils;
import com.kevin.wang.springpatternkevinwang.config.WxOpenConfig;
import com.kevin.wang.springpatternkevinwang.constant.UserConstant;
import com.kevin.wang.springpatternkevinwang.exception.BussinessException;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.model.dto.user.*;
import com.kevin.wang.springpatternkevinwang.model.vo.LoginUserVO;
import com.kevin.wang.springpatternkevinwang.model.vo.UserVO;
import com.kevin.wang.springpatternkevinwang.service.UserService;
import com.kevin.wang.springpatternkevinwang.utils.KeyUtils;
import com.kevin.wang.springpatternkevinwang.utils.ParamUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wang
 * @create 2023-05-24-0:31
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {



    @Resource
    private ParamUtils paramUtils;
    @Resource
    private UserService userService;

    @Resource
    private WxOpenConfig wxOpenConfig;

    private final String ACCESSKEY= "accessKey";
    private final String SIGN = "sign";
    private final String BODY = "body";


    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegistryRequest registerRequest,HttpServletRequest request) {
        ThrowUtils.throwIf(registerRequest==null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(!paramUtils.vilateKeyParam(request),ErrorCode.PARAMS_ERROR);
        String accessKey = request.getHeader(ACCESSKEY);
        String body = request.getHeader(BODY);
        String sign = request.getHeader(SIGN);
        accessKey = KeyUtils.generateKey(accessKey);
        String secretKey = KeyUtils.generateSign(body,sign);
        String userAccount = registerRequest.getUserAccount();
        String password = registerRequest.getPassword();
        String checkPassword = registerRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount,password,checkPassword,accessKey,secretKey)){
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        long userId = userService.userRegister(userAccount, password, checkPassword,accessKey,secretKey);
        return ResultUtils.success(userId);
    }

    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest==null, ErrorCode.PARAMS_ERROR);

        String userAccount = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount,password)){
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }

        LoginUserVO loginUserVO = userService.userLogin(userAccount, password,request);

        return ResultUtils.success(loginUserVO);
    }



    @GetMapping("/login/wx_open")
    public BaseResponse<LoginUserVO> loginByMpOpen(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) {
        try {
            WxMpService wxMpService = wxOpenConfig.getWxMpService();
            WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo userInfo = wxMpService.getOAuth2Service().getUserInfo(accessToken, code);
            String unionId = userInfo.getUnionId();
            String mpOpenId = userInfo.getOpenid();
            if(StringUtils.isAnyBlank(unionId,mpOpenId)){
                throw new BussinessException(ErrorCode.SYSTEM_ERROR, "登录失败，系统错误");
            }
            LoginUserVO loginUserVO = userService.userLoginByMpOpen(userInfo,request);
            return ResultUtils.success(loginUserVO);
        } catch (WxErrorException e) {
            throw new BussinessException(ErrorCode.SYSTEM_ERROR, "登录失败，系统错误");
        }
    }

    @GetMapping("/logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request) {
        ThrowUtils.throwIf(request==null,ErrorCode.PARAMS_ERROR);

        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUserVo(HttpServletRequest request) {
        User rootUser = userService.getById(1695396788623720450l);
        LoginUserVO loginUserVO = userService.getLoginUserVO(rootUser);
        return ResultUtils.success(loginUserVO);
    }
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.USER_ADMIN)
    public BaseResponse<Boolean> addUser(@RequestBody UserAddRequest userAddRequest,HttpServletRequest request) {
        ThrowUtils.throwIf(userAddRequest==null,ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(!paramUtils.vilateKeyParam(request), ErrorCode.OPERATION_ERROR);
        String accessKey = request.getHeader("accessKey");
        String body = request.getHeader("body");
        String sign = request.getHeader("sign");
        ThrowUtils.throwIf(StringUtils.isAnyBlank(accessKey,body,sign),ErrorCode.OPERATION_ERROR);
        User user = new User();
        BeanUtil.copyProperties(userAddRequest,user);
        user.setAccessKey(KeyUtils.generateKey(accessKey));
        user.setSecretKey(KeyUtils.generateSign(body,sign));
        boolean result = userService.save(user);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.USER_ADMIN)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest==null,ErrorCode.PARAMS_ERROR);

        Long userId = deleteRequest.getId();
        if(userId==null || userId<=0){
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean result = userService.removeById(userId);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.USER_ADMIN)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userUpdateRequest==null,ErrorCode.PARAMS_ERROR);

        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest,user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result,ErrorCode.SYSTEM_ERROR);
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.USER_ADMIN)
    public BaseResponse<User> getUserById(@RequestParam("id") Long id, HttpServletRequest request) {
        if(id==null ||id<0){
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user==null,ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    @GetMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.USER_ADMIN)
    public BaseResponse<UserVO> getUserVo(@RequestParam("id") Long id, HttpServletRequest request) {
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        UserVO userVO = userService.getUserVO(user);
        return ResultUtils.success(userVO);
    }

    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.USER_ADMIN)
    public BaseResponse<Page<User>> listUser(@RequestBody UserQueryRequest userQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userQueryRequest==null,ErrorCode.PARAMS_ERROR);

        QueryWrapper<User> queryWrapper = userService.getQueryWrapper(userQueryRequest);
        long currentPage = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();

        ThrowUtils.throwIf(pageSize>20,ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(currentPage, pageSize), queryWrapper);

        return ResultUtils.success(userPage);
    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVo(@RequestBody UserQueryRequest userQueryRequest, HttpServletRequest request) {
        long pageSize = userQueryRequest.getPageSize();
        long currentPage = userQueryRequest.getCurrent();
        ThrowUtils.throwIf(pageSize>20,ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(currentPage,pageSize),userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(currentPage,pageSize);
        List<UserVO> userVOList = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMy(@RequestBody UserUpdateMyRequest userUpdateMyRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userUpdateMyRequest==null,ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);

        User user = new User();
        BeanUtil.copyProperties(userUpdateMyRequest,user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result,ErrorCode.SYSTEM_ERROR);
        return ResultUtils.success(result);
    }
}
