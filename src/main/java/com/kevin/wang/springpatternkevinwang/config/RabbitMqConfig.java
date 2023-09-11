package com.kevin.wang.springpatternkevinwang.config;


import com.dubbo.model.enums.RabbitMQQueueEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wang
 * @create 2023-2023-09-0:30
 */
@Configuration
public class RabbitMqConfig {
    
    @Bean
    CustomExchange pluginDelayedExchange(){
        Map<String,Object> args = new HashMap<>();
        args.put("x-delayed-type","direct");
        return new CustomExchange(RabbitMQQueueEnum.QUEUE_ACCESSKEY_CHANNEL.getExchangeName(), "x-delayed-message",true,false,args);
    }

    @Bean
    public Queue accessKeyQueue(){
        return new Queue(RabbitMQQueueEnum.QUEUE_ACCESSKEY_CHANNEL.getQueueName());
    }

    @Bean
    Binding accessKeyBinding(CustomExchange pluginDelayedDirectExchange,Queue accessKeyQueue){
        return BindingBuilder
                .bind(accessKeyQueue)
                .to(pluginDelayedDirectExchange)
                .with(RabbitMQQueueEnum.QUEUE_ACCESSKEY_CHANNEL.getRouteKey())
                .noargs();
    }
}
