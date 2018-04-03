package com.cmcc.vrp.boss.jiangxi.util;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Encoder;

import com.cmcc.vrp.util.Des;
import com.cmcc.vrp.util.MD5;


/**
 * Created by leelyn on 2016/7/7.
 */
public class JxEncrypt {

    /**
     * 江西渠道BODY加密
     *
     * @param data
     * @param key
     * @return
     */
    public static String encrypt(String data, String key) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        return new BASE64Encoder().encode(des(md5(data, key), data).getBytes());
    }

    /**
     * MD5加密
     *
     * @param data
     * @param key
     * @return
     */
    private static String md5(String data, String key) {
        return MD5.sign(data, key, "UTF-8");
    }

    /**
     * des加密
     *
     * @param md5Str
     * @param data
     * @return
     */
    private static String des(String md5Str, String data) {
        try {
            return Des.decrypt(md5Str + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
