package com.kevin.wang.springpatternkevinwang.service.impl;

import com.dubbo.model.enums.RabbitMQQueueEnum;
import com.dubbo.service.AccessKeyPublishDelayedMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author wang
 * @create 2023-2023-09-10:40
 */
@DubboService
@Slf4j
public class AccessKeyPublishServiceImpl implements AccessKeyPublishDelayedMessage {
    private static final Logger logger = LoggerFactory.getLogger(AccessKeyPublishServiceImpl.class);

    @Resource
    private AmqpTemplate amqpTemplate;

    @Override
    public void sendDelayedMessage(String accessKey, long delayedTime) {
        amqpTemplate.convertAndSend(RabbitMQQueueEnum.QUEUE_ACCESSKEY_CHANNEL.getExchangeName(), RabbitMQQueueEnum.QUEUE_ACCESSKEY_CHANNEL.getRouteKey(),accessKey,(message)->{
            message.getMessageProperties().setHeader("x-delay",delayedTime*1000);
            return message;
        } );
        log.info("send delayed message accessKey {}",accessKey);
    }

    @Override
    public void sendDelayedMessage(String message) {
        sendDelayedMessage(message,60);
    }
}

