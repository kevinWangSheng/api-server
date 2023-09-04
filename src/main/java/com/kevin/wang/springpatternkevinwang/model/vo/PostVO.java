package com.kevin.wang.springpatternkevinwang.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.kevin.common.model.vo.UserVO;
import com.kevin.wang.springpatternkevinwang.model.entity.Post;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author wang
 * @create 2023-2023-21-0:10
 */
@Data
@Accessors(chain = true)
public class PostVO implements Serializable {

    private final static Gson gson = new Gson();

    private String title;

    private Long id;

    private Integer thumbNum;

    private Integer favourNum;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private List<String> tagList;

    // 创建人信息
    private UserVO user;

    private boolean hasThumb;

    private boolean hasFavour;

    public static Post voToObj(PostVO postVo){
        if(postVo==null){
            return null;
        }
        Post post = new Post();

        BeanUtil.copyProperties(postVo,post);
        List<String> tagList = postVo.getTagList();
        if(tagList!=null){
            post.setTags(gson.toJson(tagList));
        }

        return post;
    }

    public static PostVO objToVo(Post post){
        if(post==null){
            return null;
        }
        PostVO postVo  = new PostVO();
        BeanUtil.copyProperties(post,postVo);
        postVo.setTagList(gson.fromJson(post.getTags(), new TypeToken<List<String>>() {
        }.getType()));

        return postVo;
    }
}

