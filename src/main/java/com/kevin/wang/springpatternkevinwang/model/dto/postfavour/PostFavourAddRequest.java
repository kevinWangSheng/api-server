package com.kevin.wang.springpatternkevinwang.model.dto.postfavour;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wang
 * @create 2023-2023-21-11:35
 */
@Data
public class PostFavourAddRequest implements Serializable
{

    private Long postId;

    private static final long serialVersionUID = 1L;
}
