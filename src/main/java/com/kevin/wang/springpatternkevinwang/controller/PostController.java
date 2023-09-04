package com.kevin.wang.springpatternkevinwang.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.kevin.common.model.entity.User;
import com.kevin.wang.springpatternkevinwang.annotation.AuthCheck;
import com.kevin.wang.springpatternkevinwang.common.BaseResponse;
import com.kevin.wang.springpatternkevinwang.common.DeleteRequest;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.common.ResultUtils;
import com.kevin.wang.springpatternkevinwang.constant.UserConstant;
import com.kevin.wang.springpatternkevinwang.exception.BussinessException;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.model.dto.post.PostAddRequest;
import com.kevin.wang.springpatternkevinwang.model.dto.post.PostEditRequest;
import com.kevin.wang.springpatternkevinwang.model.dto.post.PostQueryRequest;
import com.kevin.wang.springpatternkevinwang.model.dto.post.PostUploadRequest;
import com.kevin.wang.springpatternkevinwang.model.entity.Post;
import com.kevin.wang.springpatternkevinwang.model.vo.PostVO;
import com.kevin.wang.springpatternkevinwang.service.PostService;
import com.kevin.wang.springpatternkevinwang.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wang
 * @create 2023-2023-23-22:44
 */
@RestController
@Slf4j
@RequestMapping("/post")
public class PostController {

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();



    @PostMapping("/add")
    public BaseResponse<Long> addPost(@RequestBody PostAddRequest postRequest, HttpServletRequest request) {
        String title = postRequest.getTitle();
        String content = postRequest.getContent();
        List<String> tags = postRequest.getTags();

        Post post = new Post();

        post.setTitle(title);
        post.setContent(content);
        if(tags!=null) {
            post.setTags(GSON.toJson(tags));
        }
        User loginUser = userService.getLoginUser(request);
        post.setUserId(loginUser.getId());
        boolean save = postService.save(post);

        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);
        Long postId = post.getId();
        return ResultUtils.success(postId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePost(DeleteRequest deleteRequest,HttpServletRequest request) {
        if(deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        Post post = postService.getById(deleteRequest.getId());
        ThrowUtils.throwIf(post==null,ErrorCode.NOT_FOUND_ERROR);
        // 只有管理员才可以删除
        if(userId!=post.getUserId() && !userService.isAdmin(request)) {
            throw new BussinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = postService.removeById(deleteRequest.getId());
        ThrowUtils.throwIf(!result,ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.USER_ADMIN)
    public BaseResponse<Boolean> updatePost(@RequestBody PostUploadRequest postRequest, HttpServletRequest request) {
        if(postRequest == null || postRequest.getId() <= 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        Long postId = postRequest.getId();
        Post post = new Post();
        BeanUtil.copyProperties(postRequest,post);
        post.setTags(GSON.toJson(postRequest.getTags()));

        postService.validPost(post,false);
        Post oldPost = postService.getById(postId);
        ThrowUtils.throwIf(oldPost==null,ErrorCode.NOT_FOUND_ERROR);
        boolean result = postService.updateById(post);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/vo")
    public BaseResponse<PostVO> getPostVo(Long postId, HttpServletRequest request) {
        if(postId == null || postId <= 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        Post post = postService.getById(postId);
        ThrowUtils.throwIf(post==null,ErrorCode.NOT_FOUND_ERROR);

        return ResultUtils.success(postService.getPostVO(post,request));
    }

    @PostMapping("/listPageVo")
    public BaseResponse<Page<PostVO>> listPostVo(@RequestBody PostQueryRequest postQueryRequest, HttpServletRequest request) {
        long currentpage = postQueryRequest.getCurrent();
        long pagesize = postQueryRequest.getPageSize();

        Page<Post> postPage = postService.page(new Page<>(currentpage, pagesize),postService.getQueryWrapper(postQueryRequest));

        return ResultUtils.success(postService.getPostVOPage(postPage,request));
    }

    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<PostVO>> listMyPageVo(@RequestBody PostQueryRequest postQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(postQueryRequest==null,ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser==null,ErrorCode.NOT_LOGIN_ERROR);
        postQueryRequest.setUserId(loginUser.getId());
        Page<Post> postPage = postService.page(new Page<>(postQueryRequest.getCurrent(), postQueryRequest.getPageSize()),
                postService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(postService.getPostVOPage(postPage,request));
    }

    @PostMapping("/serach/page/vo")
    public BaseResponse<Page<PostVO>> getPostVoPageByEs(@RequestBody PostQueryRequest postQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(postQueryRequest==null,ErrorCode.PARAMS_ERROR);
        Page<Post> postPage = postService.searchFromEs(postQueryRequest);
        return ResultUtils.success(postService.getPostVOPage(postPage,request));
    }

    @PostMapping("/edit")
    public BaseResponse<Boolean> editPost(@RequestBody PostEditRequest postEditRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(postEditRequest==null,ErrorCode.PARAMS_ERROR);

        Long postId = postEditRequest.getId();
        Post post = new Post();
        BeanUtil.copyProperties(postEditRequest,post);
        List<String> tags = postEditRequest.getTags();
        if(tags!=null){
            post.setTags(GSON.toJson(tags));
        }
        if(postId == null || postId <= 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser==null,ErrorCode.NOT_LOGIN_ERROR);
        Post oldPost = postService.getById(postId);
        Long userId = oldPost.getUserId();

        if(!userId.equals(loginUser.getId())&& !userService.isAdmin(loginUser)){
            throw new BussinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = postService.updateById(post);
        return ResultUtils.success(result);
    }
}


