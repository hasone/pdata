package com.cmcc.vrp.util;

import org.apache.commons.codec.DecoderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * AES工具类
 */
public class AES {

    public static final String ALGORITHM = "AES";

    public static SecretKey getKey(byte[] password) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password);
            keyGenerator.init(128, secureRandom);
            return keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     *
     * @param src      源文
     * @param password 密钥
     * @return 密文
     */
    public static byte[] encrypt(byte[] src, byte[] password) {
        try {
            SecretKey secretKey = getKey(password);
            byte[] encodedKey = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(encodedKey, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(src);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param encrypted 密文
     * @param password  密钥
     * @return 明文
     */
    public static byte[] decrypt(byte[] encrypted, byte[] password) {
        try {
            SecretKey secretKey = getKey(password);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(encrypted);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param encrypted 密文
     * @param password  密钥
     * @return 明文
     */
    public static String decryptString(String encrypted, String password) {
        byte[] s = DatatypeConverter.parseHexBinary(encrypted);
        byte[] decryptResults = decrypt(s, password.getBytes());
        return new String(decryptResults);
    }

    public static void main(String[] args) throws DecoderException {
        String content = "123456";
        String password = "6312834625755787264";
        // 加密
        System.out.println("加密前：" + content);
        byte[] encryptResult = encrypt(content.getBytes(), password.getBytes());

        System.out.println(Arrays.toString(content.toCharArray()));

        System.out.println("加密后的字符串:" + DatatypeConverter.printHexBinary(encryptResult));
        System.out.println("加密后的字符串:" + Arrays.toString(encryptResult));

        // 解密
        byte[] decryptResult = decrypt(encryptResult, password.getBytes());
        System.out.println("解密后：" + new String(decryptResult));

        //转成string解密
        byte[] s = DatatypeConverter.parseHexBinary(DatatypeConverter.printHexBinary(encryptResult));
        byte[] decryptResults = decrypt(s, password.getBytes());
        System.out.println("转成string解密后：" + new String(decryptResults));

    }

}
