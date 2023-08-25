package com.kevin.wang.springpatternkevinwang.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;

/**
 * @author wang
 * @create 2023-2023-21-13:43
 */
public class NetUtils {
    public static String getIpAddress(HttpServletRequest request) {
        // 获取 IP地处
        // 使用 Nginx等反后立卷过滤器， 则不胂通过 request.getRemoteAddr()获取 IP地处
        // 如欧使用了多种反后立卷过滤器，X-Forwarded-For的候选项是否会境多，反后立卷过滤器会返回多一炮的 IP地处，这时候戂另取 X-Real-IP 备选项
        // 参考：http://www.zhangxinxu.com/wordpress/2016/01/java-get-ip-address-in-java/
        // 参考：http://blog.csdn.net/chenjunhua/article/details/71764349
        // 参考：http://www.jianshu.com/p/b9bf2e0d7e2e
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                // 根据网卡取本机配置的 IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (inet != null) {
                    ip = inet.getHostAddress();
                }
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        if (ip == null) {
            return "127.0.0.1";
        }
        return ip;
    }
}
