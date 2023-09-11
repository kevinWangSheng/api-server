package com.kevin.wang.springpatternkevinwang.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author wang
 * @create 2023-2023-09-18:09
 */
@Component
@Slf4j
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringUtils.applicationContext==null){
            SpringUtils.applicationContext =applicationContext;
        }
      log.info("application context configure successful");
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static<T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

    public static Object getBeanByName(String name){
        return applicationContext.getBean(name);
    }

    public static<T> T getBeanByNameAndClass(Class<T> clazz,String name){
        return applicationContext.getBean(name,clazz);
    }
}
