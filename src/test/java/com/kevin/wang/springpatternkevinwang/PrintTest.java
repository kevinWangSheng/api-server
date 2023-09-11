package com.kevin.wang.springpatternkevinwang;

import cn.hutool.core.thread.ThreadUtil;
import com.aliyun.api.gateway.demo.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wang
 * @create 2023-2023-24-20:12
 */
public class PrintTest {
    private static AtomicInteger  atomicInteger = new AtomicInteger(0);
    public static void main(String[] args) {
        String host = "https://ali-weather.showapi.com";
        String path = "/area-to-weather-date";
        String method = "GET";
        String appcode = "813385579e1548359e2edc36c05d0c4c";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
//        querys.put("areaCode", "570100");
        querys.put("area", "海口");
        querys.put("date", "20230910");
        querys.put("need3HourForcast", "1");
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.out.println();
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        executorService.execute(()->{
//            while(atomicInteger.get() < 100){
//                System.out.println(Thread.currentThread().getName()+":"+atomicInteger.incrementAndGet());
//            }
//        });
//        executorService.execute(()->{
//            while(atomicInteger.get() < 100){
//                System.out.println(Thread.currentThread().getName()+":"+atomicInteger.incrementAndGet());
//            }
//        });
    }


}
