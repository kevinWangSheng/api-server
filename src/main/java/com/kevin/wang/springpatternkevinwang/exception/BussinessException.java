package com.kevin.wang.springpatternkevinwang.exception;

import com.kevin.wang.springpatternkevinwang.common.ErrorCode;

/**
 * @author wang
 * @create 2023-2023-20-18:14
 */
public class BussinessException extends RuntimeException{

    private int code;

    public BussinessException(int code, String message){
        super(message);
        this.code = code;
    }

    public BussinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BussinessException(ErrorCode errorCode,String message){
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode(){
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static BussinessException of(int code, String message){
        return new BussinessException(code, message);
    }

    public static BussinessException of(int code){
        return new BussinessException(code, "");
    }

    public static BussinessException Ok(String message){
        return new BussinessException(200, message);
    }

    public static BussinessException error(String message){
        return new BussinessException(500,message);
    }
}
