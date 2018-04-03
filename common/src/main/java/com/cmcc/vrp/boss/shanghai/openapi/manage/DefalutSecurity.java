package com.cmcc.vrp.boss.shanghai.openapi.manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.cmcc.vrp.boss.shanghai.openapi.model.Aedk;
import com.cmcc.vrp.boss.shanghai.openapi.model.Ask;
import com.cmcc.vrp.boss.shanghai.openapi.model.GroupKey;
import com.cmcc.vrp.boss.shanghai.openapi.model.ResponseStatus;
import com.cmcc.vrp.boss.shanghai.openapi.model.ServerResponse;
import com.cmcc.vrp.boss.shanghai.openapi.util.Base64Util;
import com.cmcc.vrp.boss.shanghai.openapi.util.CryptoUtil;
import com.cmcc.vrp.boss.shanghai.openapi.util.JsonUtil;
import com.cmcc.vrp.boss.shanghai.openapi.util.RandomUtil;


/**
 * @author lgk8023
 *
 */
public class DefalutSecurity implements SecurityI {
    private GroupKey groupKey;
    private CloseableHttpClient client = HttpClients.createDefault();
    private String url;
    private String apk;
    private PrivateKey privateKey;
    private static Map<String, Class<?>> map = new HashMap<String, Class<?>>();

    static {
        map.put("ask", Ask.class);
        map.put("aedk", Aedk.class);
    }

    public DefalutSecurity(String url, String appCode, String apk) {
        this.url = url + "/1.0/KEY/" + appCode + "/";
        this.apk = apk;
    }

    private void flushKey() throws Exception {
        groupKey = apply();
        groupKey.getAedk().getEndTime();
        groupKey.getAsk().getEndTime();
        privateKey = getPrivateKey(groupKey.getAsk().getPrivateKeyStr());
    }

    @Override
    public Ask getAsk() throws Exception {
        if (groupKey == null || groupKey.getAsk().getEndTime().getTime() < System.currentTimeMillis()) {
            flushKey();
        }
        return groupKey.getAsk();
    }

    @Override
    public Aedk getAedk() throws Exception {
        if (groupKey == null || groupKey.getAedk().getEndTime().getTime() < System.currentTimeMillis()) {
            flushKey();
        }
        return groupKey.getAedk();
    }

    @Override
    public String encrypt(String text) throws Exception {
        byte[] secretByte = Base64Util.decode(getAedk().getValue());
        SecretKey secretKey = new SecretKeySpec(secretByte, "DESede");
        String otext = CryptoUtil.encrypt(text, "DESede", secretKey);
        return otext;
    }

    @Override
    public String decrypt(String text) throws Exception {
        byte[] secretByte = Base64Util.decode(getAedk().getValue());
        SecretKey secretKey = new SecretKeySpec(secretByte, "DESede");
        String otext = CryptoUtil.decrypt(text, "DESede", secretKey);
        return otext;
    }

    @SuppressWarnings("deprecation")
    private GroupKey apply() throws Exception {
        String getUrl = url + URLEncoder.encode(createSalt(apk));
        HttpGet get = new HttpGet(getUrl);
        CloseableHttpResponse response = null;
        BufferedReader br = null;
        try {
            response = client.execute(get);
            InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
            br = new BufferedReader(isr);
            String tmp = null;
            StringBuffer sb = new StringBuffer();
            while ((tmp = br.readLine()) != null) {
                sb.append(tmp);
            }
            ServerResponse sr = JsonUtil.toBean(sb.toString(), ServerResponse.class);
            if (sr.getStatus().equals(ResponseStatus.SUCCESS.toString())) {
                byte[] secretByte = Base64Util.decode(apk);
                SecretKey secretKey = new SecretKeySpec(secretByte, "DESede");
                String otext = CryptoUtil.decrypt(sr.getResult(), "DESede", secretKey);
                GroupKey gk = JsonUtil.toBean(otext, GroupKey.class);
                return gk;
            } else {
                throw new Exception(sr.getExceptionCode().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            if (response != null) {
                response.close();
            }
            if (br != null) {
                br.close();
            }
        }
    }

    /**
     * 鍒涘缓鐩愬��
     * 
     * @param apk
     * @return
     * @throws EOPException
     */
    private String createSalt(String apk) {
        String securitySalt = null;
        String randomSalt = RandomUtil.getRandomString(30, 2);
        byte[] apkByte = Base64Util.decode(apk);
        SecretKey secretKey = new SecretKeySpec(apkByte, "DESede");
        securitySalt = CryptoUtil.encrypt(randomSalt, "DESede", secretKey);
        if (securitySalt.contains("/")||
                securitySalt.contains("+")||
                securitySalt.contains(" ")||
                securitySalt.contains("?")||
                securitySalt.contains("%")||
                securitySalt.contains("#")||
                securitySalt.contains("&")
            ) {
            securitySalt = createSalt(apk);

        }
        return securitySalt;
    }

    @Override
    public String sign(String text) throws Exception {
        java.security.Signature signaturer = java.security.Signature.getInstance("MD5withRSA");
        signaturer.initSign(privateKey);
        signaturer.update(text.getBytes());
        String signValue = Base64Util.encodeString(signaturer.sign());
        return signValue;
    }

    @Override
    public boolean verify(String text, String signValue, String publicKey) throws Exception {
        java.security.Signature signaturer = java.security.Signature.getInstance("MD5withRSA");
        signaturer.initVerify(getPublicKey(publicKey));
        signaturer.update(text.getBytes());
        return signaturer.verify(Base64Util.decode(signValue));
    }

    /**
     * 閫氳繃鍏挜鍊艰繕鍘熸垚鍏挜
     * 
     * @param publicKeyStr
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    private static PublicKey getPublicKey(String publicKeyStr) throws InvalidKeySpecException, NoSuchAlgorithmException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64Util.decode(publicKeyStr));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        return pubKey;
    }

    /**
     * 閫氳繃绉侀挜杩樺師鎴愮閽�
     * 
     * @param privateKeyStr
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    private static PrivateKey getPrivateKey(String privateKeyStr) throws InvalidKeySpecException, NoSuchAlgorithmException {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64Util.decode(privateKeyStr));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        return priKey;
    }

}
