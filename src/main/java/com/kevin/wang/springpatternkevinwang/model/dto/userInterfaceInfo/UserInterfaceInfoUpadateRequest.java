package com.kevin.wang.springpatternkevinwang.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wang
 * @create 2023-2023-21-9:44
 */
@Data
public class UserInterfaceInfoUpadateRequest implements Serializable {

    private Long id;

    /**
     * 调用用户 id
     */
    private Long userId;


    /**
     * 0-正常，1-禁用
     */
    private Integer status;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;


    private static final long serialVersionUID = 1L;
}
