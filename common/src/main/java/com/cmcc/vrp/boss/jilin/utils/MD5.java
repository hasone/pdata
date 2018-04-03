package com.cmcc.vrp.boss.jilin.utils;

import java.security.MessageDigest;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月1日 上午8:51:05
*/
public class MD5 {

    private static final String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public MD5() {
    }

	/**
	 * @param b
	 * @return
	 */
    public static String byteArrayToString(byte[] b) {
        StringBuffer bths = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            bths.append(byteToHexString(b[i]));
        }

        return bths.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return (new StringBuilder(String.valueOf(hexDigits[d1]))).append(
				hexDigits[d2]).toString();
    }

    /**
     * @param origin
     * @return
     */
    public static String toMD5(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToString(md.digest(resultString.getBytes()));
        } catch (Exception exception) {
        }
        return resultString;
    }


}