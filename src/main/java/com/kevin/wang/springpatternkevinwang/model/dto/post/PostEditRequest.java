package com.kevin.wang.springpatternkevinwang.model.dto.post;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wang
 * @create 2023-2023-21-9:44
 */
@Data
public class PostEditRequest implements Serializable {

    private Long id;

    private String title;

    private String content;

    private List<String> tags;

    private static final long serialVersionUID = 1L;

}
