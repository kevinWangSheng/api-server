package com.kevin.wang.springpatternkevinwang.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wang
 * @create 2023-2023-20-19:00
 */

@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(200,"OK"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");


    private int code;

    private String message;

    public int getCode(){
        return this.code;
    }

    public String getMessage(){
        return  this.message;
    }
}
