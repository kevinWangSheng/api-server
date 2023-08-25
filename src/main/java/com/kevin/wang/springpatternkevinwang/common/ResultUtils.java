package com.kevin.wang.springpatternkevinwang.common;

import org.apache.poi.ss.formula.functions.T;

/**
 * @author wang
 * @create 2023-2023-20-19:05
 */
public class ResultUtils {

    public static BaseResponse error(int code,String message){
        return new BaseResponse(code,message,null);
    }

    public static BaseResponse error(ErrorCode code){
        return new BaseResponse(code.getCode(), code.getMessage(),null);
    }

    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), data);
    }

    public static BaseResponse error(ErrorCode code,String message){
        return new BaseResponse(code.getCode(),message,null);
    }
}
