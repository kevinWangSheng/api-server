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
//        registry.addMapping("/**")
//                // 允许发送 Cookie
//                .allowCredentials(true)
//                .allowedHeaders("Access-Control-Allow-Origin")
//                // 放行哪些域名（必须用 patterns，否则 * 会和 allowCredentials 冲突）
//                .allowedOriginPatterns("http://localhost:8000")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//
//                .exposedHeaders("*");

        registry.addMapping("/**").allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST","PUT", "DELETE", "OPTIONS")
                .allowCredentials(true).maxAge(3600);
    }
}
