package com.kevin.wang.springpatternkevinwang.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dubbo.model.entity.User;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.service.UserService;
import jakarta.annotation.Resource;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * @author wang
 * @create 2023-2023-07-8:27
 */

public class ShiroRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(ShiroRealm.class);
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("执行授权逻辑");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        String username = token.getUsername();
        ThrowUtils.throwIf(username==null, ErrorCode.NOT_LOGIN_ERROR);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount",username);
        User user = userService.getOne(wrapper);
        if(user==null){
            return null;
        }
        return  new SimpleAuthenticationInfo(user,user.getUserPassword(),"");
    }
}
