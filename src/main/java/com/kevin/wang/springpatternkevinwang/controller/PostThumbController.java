package com.kevin.wang.springpatternkevinwang.controller;

import com.kevin.common.model.entity.User;
import com.kevin.wang.springpatternkevinwang.common.BaseResponse;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.common.ResultUtils;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.model.dto.postThumb.PostThumbAddRequest;
import com.kevin.wang.springpatternkevinwang.service.PostThumbService;
import com.kevin.wang.springpatternkevinwang.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wang
 * @create 2023-2023-24-22:07
 */
@RestController
@RequestMapping("/post_thumb")
@Slf4j
public class PostThumbController {

    @Resource
    private PostThumbService postThumbService;

    @Resource
    private UserService userService;

    @PostMapping("/")
    public BaseResponse<Integer> addPostThumb(@RequestBody PostThumbAddRequest postThumbAddRequest, HttpServletRequest request){
        ThrowUtils.throwIf(postThumbAddRequest==null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser==null, ErrorCode.NOT_LOGIN_ERROR);
        int result = postThumbService.doPostThumb(postThumbAddRequest.getPostId(), loginUser);
        return ResultUtils.success(result);
    }

}
