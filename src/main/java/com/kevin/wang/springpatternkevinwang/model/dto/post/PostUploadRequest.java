package com.kevin.wang.springpatternkevinwang.model.dto.post;

import lombok.Data;

import java.util.List;

/**
 * @author wang
 * @create 2023-2023-21-11:30
 */
@Data

public class PostUploadRequest {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}
