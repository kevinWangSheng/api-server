package com.openapi.gateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@SpringBootApplication
@EnableDubbo(scanBasePackages = "com.openapi.gateway.component.service")
@EnableDiscoveryClient
public class OpenApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenApiGatewayApplication.class, args);
    }

//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("touser", r -> r.path("/user/**")
//                        .uri("http://localhost:7529/api/user/get?id=123"))
//                .route("tobaidu",r->r.path("/baidu")
//                        .uri("https://baidu.com"))
//                .route("toyupiicu", r -> r.path("/yupiicu")
//                        // 将满足 "/yupiicu" 路径的请求转发到 "http://yupi.icu"
//                        .uri("http://yupi.icu"))
//                .build();
//    }
}
