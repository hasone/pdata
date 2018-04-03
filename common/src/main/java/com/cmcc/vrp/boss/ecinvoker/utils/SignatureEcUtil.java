package com.cmcc.vrp.boss.ecinvoker.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by leelyn on 2016/7/15.
 */
public class SignatureEcUtil {

    /**
     * Get请求下签名
     *
     * @param appSecrect
     * @return
     */
    public static String signatrue2DoGet(String appSecrect) {
        return DigestUtils.sha256Hex(appSecrect);
    }

    /**
     * Post请求下的签名
     *
     * @param body
     * @param appSecrect
     * @return
     */
    public static String signatrue2DoPost(String body, String appSecrect) {
        return DigestUtils.sha256Hex(body + appSecrect);
    }
}
