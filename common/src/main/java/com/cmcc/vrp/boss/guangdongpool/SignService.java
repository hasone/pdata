package com.cmcc.vrp.boss.guangdongpool;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 签名算法
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
public class SignService {
    private final static String PUBLIC_KEY = "AppSysPRKey";
    private final static String PRIVATE_KEY = "AppSysPUKey";
    private final static String TIMESTAMP_KEY = "TimeStamp";

    /**
     * 签名算法： md5(AppSysPRKey=XXXXXAppSysPUKey=YYYYYTimeStamp=ZZZZ)
     *
     * @param pubKey     XXXX
     * @param privateKey YYYY
     * @param timestamp  ZZZZ
     * @return 签名值
     */
    public static String sign(String pubKey, String privateKey, String timestamp) {
        String pub = concat(PUBLIC_KEY, pubKey);
        String prv = concat(PRIVATE_KEY, privateKey);
        String tms = concat(TIMESTAMP_KEY, timestamp);

        return DigestUtils.md5Hex(pub + prv + tms).toUpperCase();
    }

    private static String concat(String key, String value) {
        return key + "=" + value;
    }
}
