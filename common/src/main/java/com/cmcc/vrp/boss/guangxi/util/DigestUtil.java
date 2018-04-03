package com.cmcc.vrp.boss.guangxi.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by leelyn on 2016/9/19.
 */
public class DigestUtil {

    public static String getPwDigest(String nonce, String createdTime, String appSecret) {
//        return Base64(Hex.encodeHexString(SHA256(nonce + createdTime + appSecret)));
        return null;
    }

    /**
     * @return
     */
    public static String genNonce() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStr = sdf.format(new Date());
        String randomStr = genRandom(10000000, 99999999);
        String str = timeStr.substring(0, 8) + randomStr + timeStr.substring(8);
        char[] ca = str.toCharArray();

        for (int i = 0; i < ca.length; i++) {
            int ri = new Integer(genRandom(0, 21)).intValue();
            ca[ri] = ca[i];
        }
        String res = new String(ca);
        System.out.println(res);
        return base64Encode(res);
    }

    private static String genRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return new Integer(s).toString();
    }

    /**
     * Base64解码
     * @param dest 需要解码的字符串
     * @return   解码后字符串
     */
    private static String base64Decode(String dest) {
        if (dest == null) {
            return "";
        }
        return new String(Base64.decodeBase64(dest.getBytes()));
    }

    /**
     * Base64编码
     * @param origin  原字符串
     * @return   编码后字符串
     */
    private static String base64Encode(String origin) {
        if (origin == null) {
            return "";
        }
        return new String(Base64.encodeBase64(origin.getBytes()));
    }


    /**
     * Base64 (SHA256 (nonce + created + AppSecret))
     *
     * @param appSecret
     * @return
     */
    public static String digistAppSecret(String nonce, String created, String appSecret) {
        return base64Encode(sha256DigestHex(base64Decode(nonce) + created
                + appSecret));
    }

    /**
     * SHA256加密
     * @param origin  原字符串
     * @return        密文
     */
    private static String sha256DigestHex(String origin) {
        if (origin == null) {
            return "";
        }
        return DigestUtils.sha256Hex(origin);
    }
}
