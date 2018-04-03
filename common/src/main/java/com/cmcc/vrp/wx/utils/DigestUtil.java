package com.cmcc.vrp.wx.utils;

import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * Created by leelyn on 2017/1/6.
 */
public class DigestUtil {

    /**
     * 获得摘要
     *
     * @param data
     * @param secret
     * @return
     */
    public static String generateDigest(String data, String secret) throws Exception {
        String alg = "DESede";
        DESedeKeySpec dks = new DESedeKeySpec(secret.getBytes());

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(alg);

        SecretKey secretKey = keyFactory.generateSecret(dks);


        Cipher cipher = Cipher.getInstance(alg);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] after = cipher.doFinal(data.getBytes());

        String digest = new String(org.apache.commons.codec.binary.Base64.encodeBase64(DigestUtils.md5(after)));
        return digest;
    }
}
