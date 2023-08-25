package com.kevin.wang.springpatternkevinwang.manager;

import com.kevin.wang.springpatternkevinwang.config.CosClientConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author wang
 * @create 2023-2023-21-15:15
 */
@Component
public class CosManager {
    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    public PutObjectResult putObject(String key,String uploadPath){
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(),key,new File(uploadPath));
        return cosClient.putObject(putObjectRequest);
    }

    public PutObjectResult putObject(String key,File file){
        PutObjectRequest request = new PutObjectRequest(cosClientConfig.getBucket(),key,file);
        return cosClient.putObject(request);
    }
}
