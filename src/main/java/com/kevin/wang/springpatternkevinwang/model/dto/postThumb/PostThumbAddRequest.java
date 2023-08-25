package com.kevin.wang.springpatternkevinwang.model.dto.postThumb;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wang
 * @create 2023-2023-21-11:43
 */
@Data
public class PostThumbAddRequest implements Serializable {
    /**
     * 帖子 id
     */
    private Long postId;

    private static final long serialVersionUID = 1L;}
