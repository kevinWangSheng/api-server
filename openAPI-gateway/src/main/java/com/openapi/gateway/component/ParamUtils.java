package com.openapi.gateway.component;

import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author wang
 * @create 2023-2023-02-23:44
 */
@Component
public class ParamUtils {
    private final String RAND_NUM = "RAND_NUM_OF_ACCESSKEY:";
    @Resource
    private RedisUtils redisUtlis;
    public  boolean vilateKeyParam(HttpHeaders headers){
        Long timeStamp = Long.valueOf(headers.getFirst("timeStamp"));
        String random = headers.getFirst("random");
        String accessKey = headers.getFirst("accessKey");
        // 使用毫秒进行判断
        if(timeStamp==null || System.currentTimeMillis()-1000*60*5>=timeStamp){
            return false;
            // 判断这个随机数是否存
        }else if(!isRandVaild(random,accessKey)){
            return false;
        }else{
            // 如果这个数字还没有设置，那就进行设置。
            return true;
        }
    }

    public double saveRandomWithExpiration(String randomNum,String accessKey){
        // 使用秒进行存储
        double time = (System.currentTimeMillis() / 1000 + 5 * 60);
        redisUtlis.zsSet(RAND_NUM+":"+accessKey, randomNum, time);
        return time;
    }

    public boolean isRandVaild(String randNum,String accessKey){
        if (redisUtlis.isZMember(RAND_NUM+":"+accessKey,randNum)) {
            return false;
        }
        double randNumTime = saveRandomWithExpiration(randNum,accessKey);
        return System.currentTimeMillis()/1000 <=randNumTime;
    }

    // 这里定时调用一下，删除超过五分钟的随机数。
    public void removeExpiredTokens(){
        Set<Object> expireRandomNums = redisUtlis.getZSET(RAND_NUM);
        if(expireRandomNums!=null && !expireRandomNums.isEmpty()){
            redisUtlis.removeZSetElement(RAND_NUM,expireRandomNums.toArray());
        }
    }
}
