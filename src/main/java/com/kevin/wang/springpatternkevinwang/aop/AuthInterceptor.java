package com.kevin.wang.springpatternkevinwang.aop;

import com.kevin.wang.springpatternkevinwang.annotation.AuthCheck;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.model.entity.User;
import com.kevin.wang.springpatternkevinwang.exception.BussinessException;
import com.kevin.wang.springpatternkevinwang.model.enums.UserRoleEnums;
import com.kevin.wang.springpatternkevinwang.service.UserService;
import jakarta.annotation.Resource;
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

/**
 * @author wang
 * @create 2023-2023-20-21:29
 */
@Aspect
@Slf4j
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint proceedingJoinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        User loginUser = userService.getLoginUser(request);
        if(StringUtils.isNotBlank(mustRole)){
            UserRoleEnums roleUser = UserRoleEnums.getUser(mustRole);
            // 判断是否有这个角色
            if(roleUser==null){
                throw new BussinessException(ErrorCode.NO_AUTH_ERROR);
            }
            // 判断是否已经被封号了
            if(UserRoleEnums.BAN.equals(roleUser)){
                throw new BussinessException(ErrorCode.NO_AUTH_ERROR);
            }

            // 必须有管理员权限
            String loginUserRole = loginUser.getUserRole();
            if(UserRoleEnums.ADMIN.equals(roleUser)){
                if(!loginUserRole.equals(mustRole)){
                    throw new BussinessException(ErrorCode.NO_AUTH_ERROR);
                }
            }
        }
        // 放行执行
        return proceedingJoinPoint.proceed();
    }

    
}
