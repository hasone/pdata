package com.cmcc.vrp.boss.shanxi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.security.SecurityManager;

/**
 * Created by leelyn on 2016/9/8.
 */
public class SxEncrypt {

    private static final Logger LOGGER = LoggerFactory.getLogger(SxEncrypt.class);

    /**
     * 陕西BOSS加密
     *
     * @param data
     * @return
     */
    public static String encrypt(String data) {
        com.linkage.security.SecurityManager securityManager = new SecurityManager();
        try {
            return securityManager.Encrypt3DES(data, "abc123");
        } catch (Exception e) {
            LOGGER.error("陕西BOSS加密抛出异常:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 陕西BOSS解密
     *
     * @param data
     * @return
     */
    public static String decrypt(String data) {
        com.linkage.security.SecurityManager securityManager = new SecurityManager();
        try {
            return securityManager.Decrypt3DES(data, "abc123");
        } catch (Exception e) {
            LOGGER.error("陕西BOSS解密抛出异常:{}", e.getMessage());
        }
        return null;
    }
}
