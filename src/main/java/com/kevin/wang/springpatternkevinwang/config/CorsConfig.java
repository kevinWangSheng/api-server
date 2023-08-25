package com.kevin.wang.springpatternkevinwang.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wang
 * @create 2023-2023-21-15:49
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("PUT","GET","POST","DELETE","OPTIONS")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}
