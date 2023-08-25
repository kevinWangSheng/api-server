package com.kevin.wang.springpatternkevinwang.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author wang
 * @create 2023-2023-21-13:51
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public <T> T getBean(Class<T> clazz){
        return context.getBean(clazz);
    }

    public Object getBean(String name){
        return context.getBean(name);
    }


    public <T> T getBean(String name, Class<T> clazz){
        return context.getBean(name,clazz);
    }
}
