package com.kevin.wang.springpatternkevinwang.model.dto.interfaceInfo;

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
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    // 接口id
    private Long id;
    // 接口姓名
    private String name;
    // 接口url地址
    private String url;
    // 接口描述
    private String description;
    // 接口请求头
    private String requestHeader;
    // 接口响应头
    private String responseHeader;

    //接口的状态 0-关闭，1-开启
    private Integer status;

    // 接口请求的类型
    private String method;

    private Integer isDelete;

    private Long userId;
}
