package com.kevin.wang.springpatternkevinwang.model.dto.post;

import com.kevin.wang.springpatternkevinwang.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author wang
 * @create 2023-2023-21-11:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long notId;

    private String searchText;

    private String title;

    private String content;

    private List<String> tags;

    private List<String> orTags;

    private Long userId;

    private Long favourUserId;

    private Integer isDelete;
}
