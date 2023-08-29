package com.kevin.wang.springpatternkevinwang.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wang
 * @create 2023-2023-29-0:21
 */
@Component
public class RedisUtlis {
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    public RedisUtlis(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void setWith5Minute(String key,String value){
        set(key,value,60*5l);
    }
    public void set(String key,String value,Long expireTime){
        set(key,value,expireTime,TimeUnit.SECONDS);
    }
    public void set(String key, String value, Long expireTime, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key,value,expireTime,timeUnit);
    }

    public void setKeyForever(String key,String value){
        redisTemplate.opsForValue().set(key,value);
    }

    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public boolean exist(String key){
        return redisTemplate.opsForValue().get(key)!=null;
    }

    public void zsSet(String key,String value,Double score){
        redisTemplate.opsForZSet().add(key,value,score);
    }

    public boolean isZMember(String key,String value){
        return redisTemplate.opsForZSet().rank(key,value)!=null;
    }

    public Double getZSetScore(String key, String element){
        return redisTemplate.opsForZSet().score(key,element);
    }

    public Set<Object> getZSET(String key){
        return redisTemplate.opsForZSet().rangeByScore(key, 0, System.currentTimeMillis());
    }

    public void removeZSetElement(String key, Object... element) {
        redisTemplate.opsForZSet().remove(key,element);
    }
}
