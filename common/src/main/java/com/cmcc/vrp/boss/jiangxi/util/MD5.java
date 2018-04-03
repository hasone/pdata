package com.cmcc.vrp.boss.jiangxi.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wuhaifeng on 2016/9/7.
 */
public class MD5 {
    /**
     * @param b
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String md5(byte[] b) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(b, 0, b.length);
        return byteArrayToHexString(md5.digest());
    }

    /**
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String md5(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] b = data.getBytes("UTF8");
        md5.update(b, 0, b.length);
        return byteArrayToHexString(md5.digest());
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(byteToHexString(b[i]));
        }
        return sb.toString();
    }

    private static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}