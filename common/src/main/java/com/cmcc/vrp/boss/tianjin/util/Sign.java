package com.cmcc.vrp.boss.tianjin.util;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.asiainfo.openplatform.common.util.Base64Utils;

/**
* <p>Title: 生成签名</p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月16日 下午3:04:12
*/
public class Sign {
    private static  BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
    /**
     * @param sysParams
     * @param busiParams
     * @return
     */
    public static String generateSign(Map<String, String> sysParams, String busiParams, String publicKey) {
        Map paramsMap = new HashMap();
        paramsMap.putAll(sysParams);
        paramsMap.put("content", busiParams);
        try {
            String sign = generateSign(paramsMap, publicKey);
            return sign;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * @param paramsMap
     * @return
     * @throws Exception
     */
    public static String generateSign(Map<String, String> paramsMap, String publicKey) throws Exception {
        String md5Str = getMD5Str(paramsMap);
        return encryptByPublicKey(md5Str, publicKey);
    }
    /**
     * @param source
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String source, String publicKey)
            throws Exception {
        byte[] data = source.getBytes();
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", getInstance());
        Key publicK = keyFactory.generatePublic(x509KeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm(), getInstance());
        cipher.init(1, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        int i = 0;

        while (inputLen - offSet > 0){
            byte[] cache;
            if (inputLen - offSet > 64) {
                cache = cipher.doFinal(data, offSet, 64);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 64;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64Utils.encode(encryptedData);
    }
    
    private static String getMD5Str(Map<String, String> paramMap) throws Exception {
        String[] paramArr = (String[]) paramMap.keySet().toArray(new String[paramMap.size()]);
        Arrays.sort(paramArr);
        StringBuilder keyBuf = new StringBuilder();
        StringBuilder buf = new StringBuilder();
        for (String param : paramArr) {
            if (!("sign".equals(param))) {
                String value = (String) paramMap.get(param.trim());
                if (StringUtils.isNotBlank(value)) {
                    keyBuf.append(param).append("|");
                    buf.append(value.trim());
                }
            }
        }

        String md5Str = "";
        if (buf.length() > 0) {
            md5Str = MD5(buf.toString());
        }

        return md5Str;
    }
    
    /**
     * @param s
     * @return
     * @throws Exception
     */
    public static final String MD5(String s) throws Exception {
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        byte[] btInput = s.getBytes();

        MessageDigest mdInst = MessageDigest.getInstance("MD5");

        mdInst.update(btInput);

        byte[] md = mdInst.digest();

        int j = md.length;
        char[] str = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; ++i) {
            byte byte0 = md[i];
            str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
            str[(k++)] = hexDigits[(byte0 & 0xF)];
        }
        return new String(str);
    }
    public static synchronized BouncyCastleProvider getInstance() {  
        if (bouncyCastleProvider == null) {  
            bouncyCastleProvider = new BouncyCastleProvider();  
        }  
        return bouncyCastleProvider;  
    }
}
