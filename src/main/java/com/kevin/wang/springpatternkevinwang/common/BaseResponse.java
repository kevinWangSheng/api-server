package com.kevin.wang.springpatternkevinwang.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wang
 * @create 2023-2023-20-18:57
 */
@Data
public class BaseResponse<T>implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private T data;

    public BaseResponse() {
    }


    public BaseResponse(int code, String message,T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static BaseResponse error(ErrorCode code, String message){
        return new BaseResponse(code.getCode(), message, null);
    }

    public static BaseResponse success(String message,Object data){
        return new BaseResponse(0,message,data);
    }

    public static BaseResponse success(String message){
        return new BaseResponse(0,message,null);
    }
}
