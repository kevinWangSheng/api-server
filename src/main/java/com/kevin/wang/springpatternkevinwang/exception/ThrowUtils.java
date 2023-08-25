package com.kevin.wang.springpatternkevinwang.exception;

import com.kevin.wang.springpatternkevinwang.common.ErrorCode;

/**根据对应的条件是否满足抛出对应传入的异常
 * @author wang
 * @create 2023-2023-21-15:27
 */
public class ThrowUtils {
    public static void throwIf(boolean condition,RuntimeException runtimeException) {
        if(condition){
            throw runtimeException;
        }
    }

    public static void throwIf(boolean condition, ErrorCode errorCode) {
        if(condition){
            throw new BussinessException(errorCode);
        }
    }

    public static void throwIf(boolean condition,ErrorCode errorCode,String message) {
        if(condition){
            throw new BussinessException(errorCode.getCode(),message);
        }
    }
}
