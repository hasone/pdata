package com.cmcc.vrp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;


/**
 * @author JamieWu
 * @time 2015年12月22日下午3:27:22
 */

public class MyRSA {

    /**
     * 私钥
     */
    private RSAPrivateKey privateKey;
    /**
     * 公钥
     */
    private RSAPublicKey publicKey;

    public static String encrypt(String content, String key) throws Exception {
        byte[] bt_plaintext = content.getBytes();
        PrivateKey publicKey = getPrivateKey(key);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bt_encrypted = cipher.doFinal(bt_plaintext);
        String result = Base64.encodeBase64String(bt_encrypted);
        System.out.println("加密后：" + result);
        return result;
    }

    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static String sign(String content, String key) throws Exception {
        PrivateKey privateKey = getPrivateKey(key);
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initSign(privateKey);
        sig.update(content.getBytes("UTF-8"));
        byte[] signature = sig.sign();
        String result = Base64.encodeBase64String(signature);
        return result;
    }

    public static String encryptWithFile(String content, String keyFile) throws Exception {
        byte[] bt_plaintext = content.getBytes();
        PrivateKey publicKey = getPrivateKeyWithFile(keyFile);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bt_encrypted = cipher.doFinal(bt_plaintext);
        String result = Base64.encodeBase64String(bt_encrypted);
        System.out.println("加密后：" + result);
        return result;
    }

    public static String signWithFile(String content, String key) throws Exception {
        PrivateKey privateKey = getPrivateKeyWithFile(key);
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initSign(privateKey);
        sig.update(content.getBytes("UTF-8"));
        byte[] signature = sig.sign();
        String result = Base64.encodeBase64String(signature);
        return result;
    }
    
    // try without base64 just for test
    public static String signWithFileWithoutBase64(String content, String key) throws Exception {
        PrivateKey privateKey = getPrivateKeyWithFile(key);
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initSign(privateKey);
        sig.update(content.getBytes("UTF-8"));
        byte[] signature = sig.sign();
        String result = Base64.encodeBase64String(signature);
        return result;
    }

    public static PrivateKey getPrivateKeyWithFile(String filePath) throws Exception {
        byte[] keyBytes;
        FileInputStream fis = null;
        PrivateKey privateKey = null;
        try {
            fis = new FileInputStream(new File(filePath));
            keyBytes = (new BASE64Decoder()).decodeBuffer(fis);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }

        }
        return privateKey;

    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * 随机生成密钥对
     */
    public void genKeyPair() {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            if (keyPairGen != null) {
                keyPairGen.initialize(1024, new SecureRandom());
                KeyPair keyPair = keyPairGen.generateKeyPair();
                this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
                this.publicKey = (RSAPublicKey) keyPair.getPublic();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中输入流中加载公钥
     *
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPublicKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(String publicKeyStr) throws Exception {
        byte[] buffer = Base64.decodeBase64(publicKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
        this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    public void loadPrivateKeyPKCS1(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKeyPKCS1(sb.toString());
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    public void loadPrivateKeyPKCS1(String privateKeyStr) throws Exception {
        System.out.println("load-privateKey-PKCS#1");
        byte[] buffer = Base64.decodeBase64(privateKeyStr);
       /* RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure((ASN1Sequence) ASN1Sequence.fromByteArray(buffer));
        RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(rsaPrivKeySpec);*/
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    /**
     * 从文件中加载私钥
     *
     * @throws Exception
     */
    public void loadPrivateKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    public void loadPrivateKey(String privateKeyStr) throws Exception {
        System.out.println("load-privateKey-PKCS#8");
        byte[] buffer = Base64.decodeBase64(privateKeyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    /**
     * @param publicKey
     * @param plainTextData
     * @return 返回字节数组
     * @throws Exception
     * @Description: RAS-publicKey-加密
     * @author
     * @date 2014-11-1
     * @time 下午12:00:34
     */
    public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] enBytes = cipher.doFinal(plainTextData);
            return enBytes;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("RSA-publicKey-加密异常");
        } catch (NoSuchPaddingException e) {
            throw new Exception("RSA-publicKey-加密异常");
        }
    }

    /**
     * @param publicKey
     * @param plainTextDataBase64 【明文数据-base64编码字符串】
     * @return 返回base64编码字符串
     * @throws Exception
     * @Description: RAS-publicKey-加密
     * @author
     * @date 2014-11-1
     * @time 下午12:00:55
     */
    public String encryptBase64(RSAPublicKey publicKey, String plainTextDataBase64) throws Exception {
        byte[] plainTextData = Base64.decodeBase64(plainTextDataBase64);
        byte[] enBytes = this.encrypt(publicKey, plainTextData);
        return Base64.encodeBase64String(enBytes);
    }

    /**
     * @param privateKey
     * @param cipherTextData
     * @return 返回字节数组
     * @throws Exception
     * @Description: RSA-privateKey-解密
     * @author
     * @date 2014-11-1
     * @time 下午12:01:31
     */
    public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherTextData) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] deBytes = cipher.doFinal(cipherTextData);
            return deBytes;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("RSA-privateKey-解密异常");
        } catch (NoSuchPaddingException e) {
            throw new Exception("RSA-privateKey-解密异常");
        }
    }

    /**
     * @param privateKey
     * @param cipherTextDataBase64
     * @return 返回base64字符串
     * @throws Exception
     * @Description: RSA-privateKey-解密
     * @author
     * @date 2014-11-1
     * @time 下午12:01:57
     */
    public String decryptBase64(RSAPrivateKey privateKey, String cipherTextDataBase64) throws Exception {
        byte[] cipherTextData = Base64.decodeBase64(cipherTextDataBase64);
        byte[] deBytes = this.decrypt(privateKey, cipherTextData);
        return Base64.encodeBase64String(deBytes);
    }

    /**
     * @param privateKey
     * @param content
     * @return 返回字节数组
     * @throws Exception
     * @Description: RSA-privateKey-签名
     * @author
     * @date 2014-11-1
     * @time 下午12:07:39
     */
    public byte[] sign(RSAPrivateKey privateKey, byte[] content) throws Exception {
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(privateKey);
            signature.update(content);
            byte[] signResult = signature.sign();
            return signResult;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("RSA-privateKey-签名异常");
        }
    }

    /**
     * @param privateKey
     * @param contentBase64 【验证签名原文-base64编码字符串】
     * @return 返回base64字符串
     * @throws Exception
     * @Description: RSA-privateKey-签名
     * @author
     * @date 2014-11-1
     * @time 下午12:09:12
     */
    public String signBase64(RSAPrivateKey privateKey, String contentBase64) throws Exception {
        byte[] content = Base64.decodeBase64(contentBase64);
        byte[] signResult = this.sign(privateKey, content);
        return new String(signResult);//Base64.encodeBase64String(signResult);
    }

    /**
     * @param publicKey
     * @param content   【签名原文-字节数组】
     * @param sign      【待验证签名-字节数组】
     * @return 签名结果
     * @throws Exception
     * @Description: RSA-publicKey-验证签名
     * @author
     * @date 2014-11-1
     * @time 下午12:15:52
     */
    public boolean verify(RSAPublicKey publicKey, byte[] content, byte[] sign) throws Exception {
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(publicKey);
            signature.update(content);
            return signature.verify(sign);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("RSA-publicKey-验证签名异常");
        }
    }

    /**
     * @param publicKey
     * @param contentBase64 【签名原文-base64编码字符串】
     * @param signBase64    【待验证签名-base64编码字符串】
     * @return 签名结果
     * @throws Exception
     * @Description: RSA-publicKey-验证签名
     * @author
     * @date 2014-11-1
     * @time 下午12:18:52
     */
    public boolean verifyBase64(RSAPublicKey publicKey, String contentBase64, String signBase64) throws Exception {
        byte[] content = Base64.decodeBase64(contentBase64);
        byte[] sign = Base64.decodeBase64(signBase64);
        return this.verify(publicKey, content, sign);
    }

}
