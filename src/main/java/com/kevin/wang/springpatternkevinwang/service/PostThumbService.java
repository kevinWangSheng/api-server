package com.kevin.wang.springpatternkevinwang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dubbo.model.entity.User;
import com.kevin.wang.springpatternkevinwang.model.entity.PostThumb;

/**
 * @author wang
 * @create 2023-2023-21-23:46
 */
public interface PostThumbService extends IService<PostThumb> {
    /**
     * 点赞
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doPostThumb(long postId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    int doPostThumbInner(long userId, long postId);
}
