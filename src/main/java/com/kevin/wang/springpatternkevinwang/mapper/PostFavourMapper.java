package com.kevin.wang.springpatternkevinwang.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kevin.wang.springpatternkevinwang.model.entity.Post;
import com.kevin.wang.springpatternkevinwang.model.entity.PostFavour;
import org.apache.ibatis.annotations.Param;

/**
 * @author wang
 * @create 2023-2023-21-23:17
 */
public interface PostFavourMapper extends BaseMapper<PostFavour> {

    Page<Post> listFavourPostPage(IPage<Post> page, @Param(Constants.WRAPPER) Wrapper<Post> wrapper,@Param("favourUserId")Long favourUserId);
}
