package com.kevin.wang.springpatternkevinwang.config;


import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wang
 * @create 2023-2023-07-8:47
 */
@Configuration
public class ShiroConfig {


    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        factoryBean.setSecurityManager(securityManager);

        Map<String,String> filter = new HashMap<>();

//        filter.put("/login","anon");
//        filter.put("/*","anon");
//        filter.put("/user/login","anon");
        filter.put("/interfaceInfo/list/page","anon");

        factoryBean.setLoginUrl("/user/login");
        factoryBean.setUnauthorizedUrl("403");
        factoryBean.setFilterChainDefinitionMap(filter);
        return factoryBean;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("shiroRealm") ShiroRealm shiroRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }

    @Bean("shiroRealm")
    public ShiroRealm shiroRealm(){
        return new ShiroRealm();
    }


}
