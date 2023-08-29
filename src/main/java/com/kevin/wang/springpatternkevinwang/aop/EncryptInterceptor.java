package com.kevin.wang.springpatternkevinwang.aop;

import com.kevin.wang.springpatternkevinwang.annotation.EncryptField;
import com.kevin.wang.springpatternkevinwang.annotation.EncryptFields;
import com.kevin.wang.springpatternkevinwang.utils.KeyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author wang
 * @create 2023-2023-28-21:09
 */
@Aspect
@Component
public class EncryptInterceptor {

    @Around("@annotation(encryptFields)")
    public void encryptField(ProceedingJoinPoint joinPoint, EncryptFields encryptFields) {
        try {
            Object[] args = joinPoint.getArgs();
            for(Object arg : args) {
                if(arg==null){
                    continue;
                }
                Class<?> clazz = arg.getClass();
                Field[] fields = clazz.getDeclaredFields();
                for(Field field:fields){

                    if(field.isAnnotationPresent(EncryptField.class)){
                        field.setAccessible(true);
                        Object o = field.get(arg);
                        if(o==null){
                            continue;
                        }
                        field.set(arg, KeyUtils.generateKey((String) o));
                    }
                }
                joinPoint.proceed();
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


}
