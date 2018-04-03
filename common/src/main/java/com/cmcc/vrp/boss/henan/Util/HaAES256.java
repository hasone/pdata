package com.cmcc.vrp.boss.henan.Util;

import com.asiainfo.openplatform.common.util.SecurityUtils;
import com.cmcc.vrp.boss.henan.model.HaResult;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 河南渠道AES加密，调用SDK进行
 * Created by leelyn on 2016/6/26.
 */
public class HaAES256 {

    private static final Logger LOGGER = LoggerFactory.getLogger(HaAES256.class);

    /**
     * @param busiParam
     * @param dataSecret
     * @return
     */
    public static String encryption(String busiParam, String dataSecret) {
        LOGGER.info("开始加密河南包体");
        if (StringUtils.isBlank(busiParam) || StringUtils.isBlank(dataSecret)) {
            return null;
        }
        String exception;
        try {
            String data = SecurityUtils.encodeAES256HexUpper(busiParam, SecurityUtils.decodeHexUpper(dataSecret));
            LOGGER.info("加密后的河南请求包体:{}", data);
            return data;
        } catch (UnsupportedEncodingException e) {
            exception = e.toString();
        } catch (IllegalBlockSizeException e) {
            exception = e.toString();
        } catch (InvalidKeyException e) {
            exception = e.toString();
        } catch (BadPaddingException e) {
            exception = e.toString();
        } catch (NoSuchAlgorithmException e) {
            exception = e.toString();
        } catch (NoSuchPaddingException e) {
            exception = e.toString();
        }
        LOGGER.error("加密失败,抛出异常:{}", exception);
        return null;
    }

    /**
     * @param busiParam
     * @param dataSecret
     * @return
     */
    public static String decryption(String busiParam, String dataSecret) {
        LOGGER.info("开始解密河南包体");
        if (StringUtils.isBlank(busiParam) || StringUtils.isBlank(dataSecret)) {
            return null;
        }
        String exception;
        try {
            String data = SecurityUtils.decodeAES256HexUpper(busiParam, SecurityUtils.decodeHexUpper(dataSecret));
            LOGGER.info("解密后的河南响应包体:{}", data);
            return data;
        } catch (UnsupportedEncodingException e) {
            exception = e.toString();
        } catch (IllegalBlockSizeException e) {
            exception = e.toString();
        } catch (InvalidKeyException e) {
            exception = e.toString();
        } catch (BadPaddingException e) {
            exception = e.toString();
        } catch (NoSuchAlgorithmException e) {
            exception = e.toString();
        } catch (NoSuchPaddingException e) {
            exception = e.toString();
        }
        LOGGER.error("解密失败,抛出异常:{}", exception);
        return null;
    }

    public static void main(String[] args) {
        String data = "3F9C2FD5461932B01A4335436C1B6527AD5AB6C408F90EFE50D7" 
                + "A961C5646398B009F77E0259FD249A266B67F40380794E0F67A7FC6AD218A0A362240708" 
                + "ACF75646BA5A63BECEDF1D04A084846D7A30DA4B5583E61812B26B7C78B52D22B69C981C" 
                + "AF06B3FA9634302D0C135584362CBD867970C6C8B86FEF0C0B960288E0778CF0005F36FE814E64867E6B92A39DD28EB881568B59681421BEE123CC2615E4";
        String result = HaAES256.decryption(data, "6b2182bf0d275ce99bc64e83afbdce2b");
        HaResult haResult = new Gson().fromJson(result, HaResult.class);
        System.out.println(haResult);
    }
}
