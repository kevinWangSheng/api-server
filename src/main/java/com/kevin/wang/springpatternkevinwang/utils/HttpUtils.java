package com.kevin.wang.springpatternkevinwang.utils;

import cn.hutool.json.JSONUtil;
import com.dubbo.model.entity.InterfaceInfo;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.exception.BussinessException;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.model.dto.interfaceInfo.InterfaceInvokeRequest;
import com.kevin.wang.springpatternkevinwang.model.dto.interfaceInfo.InvokeInterfaceRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wang
 * @create 2023-2023-10-13:28
 */
@Slf4j
public class HttpUtils {
    public static HttpResponse httpExecute(String host, String path, String method, Map<String,String> headers, Map<String,String> querys, Map<String,String> body){
        try {
            if("GET".equals(method)){
                return com.aliyun.api.gateway.demo.util.HttpUtils.doGet(host,path,method,headers,querys);
            }else{
                return com.aliyun.api.gateway.demo.util.HttpUtils.doPost(host,path,method,headers,querys,body);
            }
        } catch(Exception e) {
            log.error("调用出现错误,{}", e.getMessage());
            return null;
        }
    }

    public static HttpResponse exeucteLocalOrOther(InterfaceInfo interfaceInfo, InterfaceInvokeRequest invokeRequest){
        try {
            String interfaceInfoUrl = interfaceInfo.getUrl();
            URL url = new URL(interfaceInfoUrl);
            String protocl = url.getProtocol();
            int port = url.getPort();
            String host = url.getHost();
            String path = url.getPath();
            String method = interfaceInfo.getMethod();

            //传入的请求参数
            String params = invokeRequest.getUserRequestParams();
            Map<String, String> querys = ParamUtils.paramToStringParam(JSONUtil.toBean(params, Map.class));
            Map<String,String> body = new HashMap<>(querys);
            String requestHeader = interfaceInfo.getRequestHeader();
            Map<String,String> requestHeaderMap = JSONUtil.toBean(requestHeader, Map.class);
            HttpResponse response = HttpUtils.httpExecute(protocl+"://"+host+":"+port, path, method, requestHeaderMap, querys, body);
            ThrowUtils.throwIf(response==null,ErrorCode.SYSTEM_ERROR);
            return response;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
