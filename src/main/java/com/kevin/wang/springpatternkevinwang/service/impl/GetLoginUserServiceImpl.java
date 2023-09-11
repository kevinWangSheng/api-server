package com.kevin.wang.springpatternkevinwang.service.impl;

import com.dubbo.model.GetLoginUser;
import com.dubbo.model.entity.User;
import com.kevin.wang.springpatternkevinwang.utils.SpringUtils;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

/**
 * @author wang
 * @create 2023-2023-07-22:50
 */
@DubboService
public class GetLoginUserServiceImpl implements GetLoginUser {


    @Override
    public User getLoginUser() {
        SecurityUtils.setSecurityManager(SpringUtils.getBean(DefaultWebSecurityManager.class));
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        return loginUser;
    }
}
