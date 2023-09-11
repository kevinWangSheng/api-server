package com.kevin.wang.springpatternkevinwang.aop;

import cn.hutool.core.date.StopWatch;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

/**
 * @author wang
 * @create 2023-2023-20-22:31
 */
@Aspect
@Component
@Slf4j
public class LoginInterceptor {

//    @Around("execution(* com.kevin.wang.springpatternkevinwang.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 创建一个计时器用来即使用
        StopWatch watch = new StopWatch();
        // 开始计时
        watch.start();
        // 随机生成一个id作为requestId
        String requestId = UUID.randomUUID().toString();

        // 获取请求的信息
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 获取执行方法的参数
        Object[] args = point.getArgs();

        String param = "["+StringUtils.join(args,",")+"]";

        log.info("the request is :{},the param is{}, ip is :{},the url is {}",requestId,param,request.getRemoteHost(),request.getRequestURL());

        // 执行对应的方法
        Object result = point.proceed();

        watch.stop();
        long totalTimeMillis = watch.getTotalTimeMillis();
        // 最后输出执行的时间
        log.info("request end , id :{}, cost:{} ms",requestId,totalTimeMillis);

        return result;
    }
}
