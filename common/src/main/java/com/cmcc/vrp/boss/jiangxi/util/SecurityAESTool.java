package com.cmcc.vrp.boss.jiangxi.util;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author lgk8023
 *
 */
public class SecurityAESTool {

    /**
     * AES加密
     * 
     * @param str
     * @param key
     * @return
     */
    public static String encrypt(String str, String key) {
        byte[] crypted = null;
        System.out.println("加密前的消息体：");
        System.out.println(str);

        try {
            String md5str = MD5.md5(str);
            System.out.println("MD5后的签名：");
            System.out.println(md5str);

            SecretKeySpec skey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);

            String enStr = md5str + str;
            crypted = cipher.doFinal(enStr.getBytes("UTF-8"));

            System.out.println("AES加密后的字符数组：");
            printBytes(crypted);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        String body = new String(Base64.encodeBase64(crypted));
        System.out.println("BASE64后的字符串：");
        System.out.println(body);

        return body;
    }

    /**
     * AES解密
     * 
     * @param input
     * @param key
     * @return
     */
    public static String decrypt(String input, String key) {
        byte[] output = null;
        String body = null;

        System.out.println("对方传来的消息源串：");
        System.out.println(input);

        if (input == null || key == null) {
            return null;
        }

        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);

            byte[] b = Base64.decodeBase64(input);
            System.out.println("Base64解码之后的结果：");
            printBytes(b);
            // 解密
            output = cipher.doFinal(b);
            System.out.println("AES解密后的结果：");
            String md5body = new String(output, "UTF-8");
            System.out.println(md5body);

            if (md5body.length() < 32) {
                System.out.println("错误！消息体长度小于数字签名长度!");
                return null;
            }

            String md5Client = md5body.substring(0, 32);
            System.out.println("对方传来的数字签名：");
            System.out.println(md5Client);

            body = md5body.substring(32);
            System.out.println("对方传来的消息体：");
            System.out.println(body);

            String md5Server = MD5.md5(body);
            System.out.println("我方对传来消息的数字签名：");
            System.out.println(md5Server);

            if (!md5Client.equals(md5Server)) {
                System.out.println("错误！数字签名不匹配:");
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return body;
    }

    /**
     * 打印字节数组
     * 
     * @param b
     *            byte[]
     */
    public static void printBytes(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append("[" + b[i] + "],");
            if (i % 15 == 0) {
                sb.append("\n");
            }
        }
        System.out.println(sb);
    }

    public static void main(String[] args) {
        String key = "932CF2969EE6FC1B";
        String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><BODY><RESULTCODE>1</RESULTCODE><RESULTMSG>企业账户余额不足</RESULTMSG></BODY>";

        String enStr = SecurityAESTool.encrypt(data, key);

        decrypt(enStr, key);
    }
}