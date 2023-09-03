package com.kevin.wang.springpatternkevinwang.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wang
 * @create 2023-2023-21-9:43
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {
    private Long id;

    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

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
