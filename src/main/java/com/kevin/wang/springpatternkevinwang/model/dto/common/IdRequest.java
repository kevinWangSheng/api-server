package com.kevin.wang.springpatternkevinwang.model.dto.common;

import com.kevin.wang.springpatternkevinwang.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wang
 * @create 2023-2023-30-23:39
 */
@Data
public class IdRequest extends PageRequest implements Serializable {
    private Long id;

    private static final long serialVersionUID = 1L;
}
