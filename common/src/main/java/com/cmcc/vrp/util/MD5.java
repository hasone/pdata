package com.cmcc.vrp.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;

public class MD5 {

    /**
     * 16进制字符集，用于将MD5算法处理后二进制字节流转换为16进制字符串
     */
    static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6',
        '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static final Log logger = LogFactory.getLog(MD5.class);

    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + key;
        return DigestUtils.md5DigestAsHex(getContentBytes(text, input_charset));
    }

    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param sign          签名结果
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key,
                                 String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5DigestAsHex(getContentBytes(text,
            input_charset));
        return mysign.equals(sign);
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:"
                + charset);
        }
    }

    /**
     * 将指定的字符串使用md5算法处理后返回16进制字符串。
     * <p>
     *
     * @param text
     * @return
     */
    public static String md5(String text) {
        byte[] bytes = text.getBytes();
        return md5(bytes);
    }

    /**
     * 将指定的字符串使用md5算法处理后返回16进制字符串。
     * <p>
     *
     * @param bytes
     * @return
     */
    public static String md5(byte[] bytes) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            md.update(bytes);
            // 获得密文
            byte[] encrypted = md.digest();

            return toHex(encrypted);
        } catch (Exception e) {
            //logger.equals(e);
            logger.error(e);
            return null;
        }
    }

    /**
     * 获取md5算法实例
     * <p>
     *
     * @return
     */
    public static MessageDigest getMD5Instance() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            //logger.equals(e);
            logger.error(e);
            return null;
        }
    }

    /**
     * 将字节数组转化为16进制字符串
     * <p>
     *
     * @param bytes
     * @return
     */
    public static String toHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        // 把密文转换成十六进制的字符串形式
        int j = bytes.length;
        char[] str = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = bytes[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    public static String HMACSign(String text, String key) {
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
            byte[] secretByte = key.getBytes("UTF-8");
            byte[] dataBytes = text.getBytes("UTF-8");
            SecretKey secret = new SecretKeySpec(secretByte, "HMACSHA256");
            mac.init(secret);
            byte[] doFinal = mac.doFinal(dataBytes);
            byte[] hexB = new Hex().encode(doFinal);
            String checksum = new String(hexB);
            return checksum;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 32位小写MD5加密
     * @param text
     * @return
     */
    public static String encodeLowerCase(String text){  
        
        try {  
            MessageDigest digest = MessageDigest.getInstance("md5");  
            byte[] result = digest.digest(text.getBytes());  
            StringBuilder sb =new StringBuilder();  
            for(byte b:result){  
                int number = b&0xff;  
                String hex = Integer.toHexString(number);  
                if(hex.length() == 1){  
                    sb.append("0"+hex);  
                }else{  
                    sb.append(hex);  
                }  
            }  
            return sb.toString();  
        } catch (NoSuchAlgorithmException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
      
        return "" ;  
    }  
    /**
     * 32位大写MD5加密
     * @param text
     * @return
     */
    public static String encodeUpCase(String text){  
        return encodeLowerCase(text).toUpperCase();        
    }

    public static void main(String[] args) {
        String timeString = Long.toString(new Date().getTime());
        String rawString = "<BusiData><CreateTime>" + timeString
            + "</CreateTime><ChargePhoneNum>18302363080</ChargePhoneNum><ProductCode>gl_tgdz_70MS</ProductCode><ChargeNum>1</ChargeNum></BusiData>";
        String key = "52e6371acb05415e95a183c6047676c9";
        System.out.println(timeString);
        String result = MD5.sign(rawString, key, "utf-8");
        System.out.println(result);

    }
}