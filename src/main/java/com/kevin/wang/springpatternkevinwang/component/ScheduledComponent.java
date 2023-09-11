package com.kevin.wang.springpatternkevinwang.component;

import com.dubbo.model.GetLoginUser;
import com.dubbo.model.entity.User;
import com.kevin.wang.springpatternkevinwang.utils.ParamUtils;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wang
 * @create 2023-2023-06-22:02
 */
//@Component
public class ScheduledComponent {
    @Resource
    private ParamUtils paramUtils;

    @Resource
    private GetLoginUser loginUserService;

    @Scheduled(fixedRate = 5*60*1000)
    @Async
    public void deleteRandomNumOfRDXInRedis() {
        User loginUser = loginUserService.getLoginUser();
        if(loginUser==null){
            return;
        }
        System.out.println("execute the schedule of delete the expire randNum in redis");
        paramUtils.removeExpiredTokens(loginUser.getAccessKey());
    }
}
