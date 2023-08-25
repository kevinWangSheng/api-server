package com.kevin.wang.springpatternkevinwang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = "com.kevin.wang.springpatternkevinwang.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class SpringPatternKevinWangApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPatternKevinWangApplication.class, args);
    }

}
