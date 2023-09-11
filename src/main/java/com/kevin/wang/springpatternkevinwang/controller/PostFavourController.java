package com.kevin.wang.springpatternkevinwang.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dubbo.model.entity.User;
import com.kevin.wang.springpatternkevinwang.common.BaseResponse;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.common.ResultUtils;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.model.dto.post.PostQueryRequest;
import com.kevin.wang.springpatternkevinwang.model.dto.postfavour.PostFavourAddRequest;
import com.kevin.wang.springpatternkevinwang.model.dto.postfavour.PostFavourQueryRequest;
import com.kevin.wang.springpatternkevinwang.model.entity.Post;
import com.kevin.wang.springpatternkevinwang.model.vo.PostVO;
import com.kevin.wang.springpatternkevinwang.service.PostFavourService;
import com.kevin.wang.springpatternkevinwang.service.PostService;
import com.kevin.wang.springpatternkevinwang.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author wang
 * @create 2023-2023-24-21:43
 */
@RestController
@RequestMapping("/post_favour")
@Slf4j
public class PostFavourController {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @Resource
    private PostFavourService postFavourService;

    /**
     * 主要用来收藏或者取消收藏帖子
     * @param postFavourAddRequest
     * @param request
     * @return
     */
    @PostMapping("/")
    public BaseResponse<Integer> addFavour(@RequestBody PostFavourAddRequest postFavourAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(postFavourAddRequest==null || postFavourAddRequest.getPostId()==null ,ErrorCode.PARAMS_ERROR);

        User loginUser = userService.getLoginUser(request);

        ThrowUtils.throwIf(loginUser==null, ErrorCode.NOT_LOGIN_ERROR);
        int result = postFavourService.doPostFavourInner(loginUser.getId(), postFavourAddRequest.getPostId());
        return ResultUtils.success(result);
    }


    @PostMapping("/my/list/page")
    public BaseResponse<Page<PostVO>> myPostListPage(@RequestBody PostQueryRequest postQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(postQueryRequest==null , ErrorCode.PARAMS_ERROR);
        long currentPage = postQueryRequest.getCurrent();
        long pageSize = postQueryRequest.getPageSize();
        ThrowUtils.throwIf(pageSize>=20, ErrorCode.PARAMS_ERROR); // 进制爬虫
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser==null, ErrorCode.NOT_LOGIN_ERROR);
        Page<Post> postPage = postFavourService.listFavourPostByPage(new Page<>(currentPage, pageSize), postService.getQueryWrapper(postQueryRequest), loginUser.getId());
        Page<PostVO> postVOPage = postService.getPostVOPage(postPage, request);
        return ResultUtils.success(postVOPage);
    }


    @PostMapping("/list/page")
    public BaseResponse<Page<PostVO>> postListPage(@RequestBody PostFavourQueryRequest postQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(postQueryRequest==null , ErrorCode.PARAMS_ERROR);
        long currentPage = postQueryRequest.getCurrent();
        long pageSize = postQueryRequest.getPageSize();
        ThrowUtils.throwIf(pageSize>=20, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser==null, ErrorCode.NOT_LOGIN_ERROR);
        Page<Post> postPage = postFavourService.listFavourPostByPage(new Page<>(currentPage, pageSize), postService.getQueryWrapper(postQueryRequest.getPostQueryRequest()), loginUser.getId());
        Page<PostVO> postVOPage = postService.getPostVOPage(postPage, request);
        return ResultUtils.success(postVOPage);
    }

}
