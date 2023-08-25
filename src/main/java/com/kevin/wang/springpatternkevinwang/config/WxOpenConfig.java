package com.kevin.wang.springpatternkevinwang.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wang
 * @create 2023-2023-21-17:40
 */
@Configuration
@ConfigurationProperties(prefix = "wx.open")
@Slf4j
@Data
public class WxOpenConfig {

    private String appId;

    private String appSecret;

    private WxMpService wxMpService;

    public WxMpService getWxMpService() {
        if(wxMpService!=null){
            return wxMpService;
        }
        synchronized (this){
            if(wxMpService!=null){
                return wxMpService;
            }
            WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
            wxMpDefaultConfig.setAppId(appId);
            wxMpDefaultConfig.setSecret(appSecret);
            WxMpService service = new WxMpServiceImpl();
            service.setWxMpConfigStorage(wxMpDefaultConfig);
            wxMpService = service;
            return wxMpService;
        }

    }
}
