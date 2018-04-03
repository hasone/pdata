package com.cmcc.vrp.boss.jilin.utils;

import it.sauronsoftware.base64.Base64;



/**
 * <p>
 * BASE64编码解码工具�?
 * </p>
 * <p>
 * 依赖javabase64-1.3.1.jar
 * </p>
 * 
 * @author IceWee
 * @date 2012-5-19
 * @version 1.0
 */
public class Base64Utils {

    /**
     * 文件读取缓冲区大�?
     */
    private static final int CACHE_SIZE = 1024;
    
    /**
     * <p>
     * BASE64字符串解码为二进制数�?
     * </p>
     * 
     * @param base64
     * @return
     * @throws Exception
     */
    public static byte[] decode(String base64) throws Exception {
        return Base64.decode(base64.getBytes());
    }
    
    /**
     * <p>
     * 二进制数据编码为BASE64字符�?
     * </p>
     * 
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(byte[] bytes) throws Exception {
        return new String(Base64.encode(bytes));
    }


    
    
}