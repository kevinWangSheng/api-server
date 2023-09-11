package com.kevin.wang.springpatternkevinwang.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

import java.io.UnsupportedEncodingException;
import java.net.http.HttpRequest;

/**
 * @author wang
 * @create 2023-2023-28-20:24
 */
public class KeyUtils {
    private static final String ENCRYPTION_KEY = "encryptionKey";
    private static final Digester sha256 = new Digester(DigestAlgorithm.SHA256);
    public static String generateKey(String key){
        return sha256.digestHex((ENCRYPTION_KEY + key));
    }

    public static String generateSign(String body,String sign) {
        return sha256.digestHex(body + ":" + sign);
    }
    HttpRequest request;

}
