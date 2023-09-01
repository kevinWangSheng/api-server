package com.kevin.wang.springpatternkevinwang.model.dto.interfaceInfo;

import lombok.Data;

/**
 * @author wang
 * @create 2023-2023-01-0:14
 */
@Data
public class InterfaceInvokeRequest {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

    private static final long serialVersionUID = 1L;
}
