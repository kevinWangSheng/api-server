package com.kevin.wang.springpatternkevinwang.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wang
 * @create 2023-2023-21-15:30
 */
@Data
@ConfigurationProperties(prefix = "cos.client")
@Configuration
public class CosClientConfig {
    private String accessKey;

    private String secretKey;

    private String bucket;

    private String bucketName;

    private String region;

    @Bean
    public COSClient cosClient(){
        COSCredentials cred = new BasicCOSCredentials(accessKey,secretKey);
        // 2 设置bucket的北位
        ClientConfig regionConfig = new ClientConfig(new Region(region));
        COSClient cosClient = new COSClient(cred,regionConfig);
        return cosClient;
    }
}
