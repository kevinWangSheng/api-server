package com.kevin.wang.springpatternkevinwang.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kevin.wang.springpatternkevinwang.model.dto.post.PostQueryRequest;
import com.kevin.wang.springpatternkevinwang.model.entity.Post;
import com.kevin.wang.springpatternkevinwang.model.vo.PostVO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author wang
 * @create 2023-2023-21-23:46
 */
public interface PostService extends IService<Post> {
    void validPost(Post post,boolean add);

    QueryWrapper<Post> getQueryWrapper(PostQueryRequest queryRequest);

    Page<Post> searchFromEs(PostQueryRequest queryRequest);

    PostVO getPostVO(Post post, HttpServletRequest request);

    Page<PostVO> getPostVOPage(Page<Post> postPage, HttpServletRequest request);
}
