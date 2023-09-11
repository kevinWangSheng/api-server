package com.kevin.wang.springpatternkevinwang.component;

import com.dubbo.model.enums.RabbitMQQueueEnum;
import com.kevin.wang.springpatternkevinwang.constant.CommonConstant;
import com.kevin.wang.springpatternkevinwang.utils.ParamUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author wang
 * @create 2023-2023-09-11:10
 */
@Component
@RabbitListener(queues = CommonConstant.QUEUE_ACCESSKEY_CHANNEL_QUEUE)
public class DeleteUserInvokRandomNum {
    private static final Logger logger = LoggerFactory.getLogger(DeleteUserInvokRandomNum.class);
    @Resource
    private ParamUtils paramUtils;

    @RabbitHandler
    public void handler(String accessKey){
        if(accessKey==null){
            return;
        }
        logger.info("receive delay message accessKey:{}, and start to delete the user's invoke randon numver",accessKey);
        paramUtils.removeExpiredTokens(accessKey);
    }

}
