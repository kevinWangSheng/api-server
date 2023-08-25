package com.kevin.wang.springpatternkevinwang.config;

import org.apache.coyote.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.request.RequestContextHolder;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author wang
 * @create 2023-2023-21-16:58
 */
//@Configuration
//@EnableSwagger2
@Profile(value = {"dev","test"})
public class Knife4jConfig {

//    @Bean
//    public Docket default2pi2(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(new ApiInfoBuilder().title("接口文档")
//                        .description("springboot-init-project")
//                        .version("1.0")
//                        .build())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.kevin.wang.springpatternkevinwang.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
}
