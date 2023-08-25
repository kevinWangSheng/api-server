package com.kevin.wang.springpatternkevinwang.model.dto.postfavour;

import com.kevin.wang.springpatternkevinwang.common.PageRequest;
import com.kevin.wang.springpatternkevinwang.model.dto.post.PostQueryRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wang
 * @create 2023-2023-21-11:39
 */
@Data
public class PostFavourQueryRequest extends PageRequest implements Serializable {
    private PostQueryRequest  postQueryRequest;

    private Long userId;

    private Long serialVersionUID = 1L;


}
