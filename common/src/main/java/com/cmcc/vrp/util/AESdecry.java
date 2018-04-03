package com.cmcc.vrp.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author sunnychn
 * @Time：2016年3月23日 上午10:32:46
 * @version 1.0
 */
public class AESdecry {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(AESdecry.class);

    // static String e = "9238513401340235";
    static byte[] ivByte = { 85, 60, 12, 116, 99, (byte) (-67 + 256), (byte) (-83 + 256), 19, (byte) (-118 + 256),
        (byte) (-73 + 256), (byte) (-24 + 256), (byte) (-8 + 256), 82, (byte) (-24 + 256), (byte) (-56 + 256),
        (byte) (-14 + 256) };


    /**
     * 加密
     */
    public static String encrypt(String src, String key){
        try{
            if (key == null) {
                LOGGER.info("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (key.length() != 16) {
                LOGGER.info("Key长度不是16位");
                return null;
            }
            byte[] raw = key.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"0102030405060708
            IvParameterSpec ivps = new IvParameterSpec(ivByte);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivps);
    
            byte[] encrypted = cipher.doFinal(src.getBytes("UTF-8"));
    
    
            return Base64.encodeBase64String(encrypted);
        }catch(IllegalBlockSizeException e){
            LOGGER.info(e.toString());
            return "";
        }catch(BadPaddingException e){
            LOGGER.info(e.toString());
            return "";
        }catch(UnsupportedEncodingException e){
            LOGGER.info(e.toString());
            return "";
        }catch(NoSuchAlgorithmException e){
            LOGGER.info(e.toString());
            return "";
        }catch(NoSuchPaddingException e){
            LOGGER.info(e.toString());
            return "";
        }catch(InvalidKeyException e){
            LOGGER.info(e.toString());
            return "";
        }catch(InvalidAlgorithmParameterException e){
            LOGGER.info(e.toString());
            return "";
        }      
    }

//  // 解密
    /**
     * 解密
     */
    public static String decrypt(String src, String key){
        try{
            if(StringUtils.isBlank(src)){
                LOGGER.info("收到数据为空");
                return null;
            }
              // 判断Key是否正确
            if (key == null) {
                LOGGER.info("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (key.length() != 16) {
                LOGGER.info("Key长度不是16位");
                return null;
            }
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivps = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivps);
            // byte[] encrypted1 = Base64.decode(src);// 先用base64解密
            byte[] encrypted1 = Base64.decodeBase64(src.getBytes("utf-8"));
            
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        }catch(IllegalBlockSizeException e){
            LOGGER.info(e.toString());
            return "";
        }catch(BadPaddingException e){
            LOGGER.info(e.toString());
            return "";
        }catch(UnsupportedEncodingException e){
            LOGGER.info(e.toString());
            return "";
        }catch(NoSuchAlgorithmException e){
            LOGGER.info(e.toString());
            return "";
        }catch(NoSuchPaddingException e){
            LOGGER.info(e.toString());
            return "";
        }catch(InvalidKeyException e){
            LOGGER.info(e.toString());
            return "";
        }catch(InvalidAlgorithmParameterException e){
            LOGGER.info(e.toString());
            return "";
        }
    }

    public static void main(String[] args) throws DecoderException {
        String content = "123456";
        String password = "5xeYAtq6jJ28X5AF";
        System.out.println("key length：" + password.length());
        // 加密
        System.out.println("加密前：" + content);
        String content1 = encrypt(content, password);
        System.out.println("加密后的字符串:" +content1);

        // 解密
        String content2 = decrypt(content1, password);
        //转成string解密
        System.out.println("转成string解密后：" + content2);

    }
}
