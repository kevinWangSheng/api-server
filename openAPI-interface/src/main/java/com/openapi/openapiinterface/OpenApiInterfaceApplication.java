package com.openapi.openapiinterface;

import com.example.oppenapiclientsdk.OpenApiClientConfig;
import com.example.oppenapiclientsdk.client.OpenApiClient;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OpenApiInterfaceApplication {
    @Resource
    OpenApiClientConfig openApiClientConfig;
    public static void main(String[] args) {
        SpringApplication.run(OpenApiInterfaceApplication.class, args);
    }

}
