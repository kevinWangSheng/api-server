package com.kevin.wang.springpatternkevinwang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kevin.wang.springpatternkevinwang.model.entity.Post;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author wang
 * @create 2023-2023-21-23:12
 */
public interface PostMapper extends BaseMapper<Post> {

    List<Post> listPostWithDelete(@Param("minDate")Date fiveMinuteAgo);
}
