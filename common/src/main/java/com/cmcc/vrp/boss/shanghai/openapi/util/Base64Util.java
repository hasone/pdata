package com.cmcc.vrp.boss.shanghai.openapi.util;

import org.apache.commons.codec.binary.Base64;

/**
 * @author zhanggt(zhanggt@primeton.com)
 * 
 */
public class Base64Util {

    private Base64Util() {
    }

    /**
     * @param data
     * @return
     */
    public static byte[] encode(byte[] data) {
        return Base64.encodeBase64(data);
    }

    /**
     * @param data
     * @return
     */
    public static byte[] decode(byte[] data) {
        return Base64.decodeBase64(data);
    }

    /**
     * @param data
     * @return
     */
    public static byte[] decode(String data) {
        return Base64.decodeBase64(data);
    }

    /**
     * @param data
     * @return
     */
    public static String encodeString(byte[] data) {
        return Base64.encodeBase64String(data);
    }

    /**
     * @param data
     * @return
     */
    public static boolean isBase64(String data) {
        return Base64.isBase64(data);
    }
}