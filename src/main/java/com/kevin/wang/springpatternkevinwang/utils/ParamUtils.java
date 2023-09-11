package com.kevin.wang.springpatternkevinwang.utils;

import com.dubbo.model.dto.InvokeKeyDto;
import com.dubbo.model.entity.User;
import com.dubbo.service.InnerUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author wang
 * @create 2023-2023-29-0:06
 */
@Component
public class ParamUtils {
    private final String RAND_NUM = "RAND_NUM_OF_ACCESSKEY:";

    @Resource
    private RedisUtlis redisUtlis;

    @Resource
    private InnerUserService userService;
    public  boolean vilateKeyParam(InvokeKeyDto invokeKeyDto){
        if(invokeKeyDto==null){
            return false;
        }
        Long timeStamp = invokeKeyDto.getTimeStamp();
        String random = invokeKeyDto.getRandonNum();
        String accessKey = invokeKeyDto.getAccessKey();
        String sign = invokeKeyDto.getSign();
        String body = invokeKeyDto.getBody();
        User invokeUser = userService.getInvokeUser(accessKey);
        if(invokeUser==null){
            return false;
        }
        String secretKey = KeyUtils.generateSign(body, invokeUser.getSecretKey());

        // 使用毫秒进行判断
        if(timeStamp==null || System.currentTimeMillis()-1000*60*5>=timeStamp){
            return false;
            // 判断这个随机数是否存
        }else if(!isRandVaild(random,accessKey)){
            return false;
            // 判断秘钥是否正确
        }else if(!sign.equals(secretKey)){
            return false;
        }else {
            // 如果这个数字还没有设置，那就进行设置。
            return true;
        }
    }

    public  boolean vilateKeyParam(HttpServletRequest request){
        Long timeStamp = Long.valueOf(request.getHeader("timeStamp"));
        if(timeStamp==null){
            return true;
        }
        String random = request.getHeader("random");

        // 使用毫秒进行判断
        if(timeStamp==null || System.currentTimeMillis()-1000*60*5>=timeStamp){
            return false;
            // 判断这个随机数是否存
        }
        if(random==null){
            return true;
        }
        if(redisUtlis.get(RAND_NUM+random)!=null){
            return false;
        }
            // 如果这个数字还没有设置，那就进行设置。设置成为60s过期
        redisUtlis.set(RAND_NUM+random,random,60l);
        return true;

    }



    public double saveRandomWithExpiration(String randomNum,String accessKey){
        // 使用秒进行存储
        double time = (System.currentTimeMillis() / 1000 + 5 * 60);
        redisUtlis.zsSet(RAND_NUM+accessKey, randomNum, time);
        return time;
    }

    public boolean isRandVaild(String randNum,String accessKey){
        if (redisUtlis.isZMember(RAND_NUM+accessKey,randNum)) {
            return false;
        }
        double randNumTime = saveRandomWithExpiration(randNum,accessKey);
        return System.currentTimeMillis()/1000 <=randNumTime;
    }

    // 这里定时调用一下，删除超过五分钟的随机数。
    public void removeExpiredTokens(String accessKey){
        Set<Object> expireRandomNums = redisUtlis.getZSET(RAND_NUM+accessKey);
        if(expireRandomNums!=null && !expireRandomNums.isEmpty()){
            redisUtlis.removeZSetElement(RAND_NUM+accessKey,expireRandomNums.toArray());
        }
    }

    public static Map<String,String> paramToStringParam(Map map){
        Map<String,String> stringMap = new HashMap<>();
        for(Object key:map.keySet()){
            stringMap.put(String.valueOf(key),String.valueOf(map.get(key).toString()));
        }
        return stringMap;
    }
}
