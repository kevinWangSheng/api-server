package com.openapi.gateway.component;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @author wang
 * @create 2023-2023-03-0:06
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
}
