package com.kevin.wang.springpatternkevinwang.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wang
 * @create 2023-2023-21-9:44
 */
@Data
public class InterfaceInfoEditRequest implements Serializable {

    private Long id;

    private String name;

    private String url;

    private String method;

    private String requestHeader;

    private String responseHeader;

    private Integer status;

    private String description;

    private Long userId;

    private static final long serialVersionUID = 1L;

}
