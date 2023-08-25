package com.kevin.wang.springpatternkevinwang.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author wang
 * @create 2023-2023-21-16:05
 */
@JsonComponent
public class JsonConfig {

    /**
     * 配置Long类型转化json时丢失精度问题
     * @param builder
     * @return
     */
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder){
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        SimpleModule module = new SimpleModule();

        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE,ToStringSerializer.instance);

        objectMapper.registerModule(module);

        return objectMapper;
    }
}
