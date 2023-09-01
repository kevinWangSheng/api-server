package com.openapi.openapiinterface;

import com.example.oppenapiclientsdk.client.OpenApiClient;
import com.example.oppenapiclientsdk.model.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenApiInterfaceApplicationTests {

    @Resource
    OpenApiClient openApiClient;
    @Test
    void contextLoads() {
        System.out.println(openApiClient.getNameByGet("wangwu"));

        System.out.println(openApiClient.getNameByPost("laoliu"));

        User user = new User();
        user.setUserName("wangwu");
        System.out.println(openApiClient.getUserNameByPost(user));
    }

}
