package com.cmcc.vrp.boss.henan.Util;

import com.asiainfo.openplatform.common.util.SecurityUtils;
import com.asiainfo.openplatform.common.util.SignUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 河南省渠道数字签名，调用SDK进行
 * Created by leelyn on 2016/6/26.
 */
public class HaSign {

    /**
     * @param method
     * @param appId
     * @param key
     * @param accessToken
     * @param busiParam
     * @param timestamp
     * @return
     */
    public static String sign(String method, String appId, String key, String accessToken, String busiParam, String timestamp) {
        Map<String, String> sysParam = new HashMap<String, String>();
        sysParam.put("method", method);
        sysParam.put("format", "json");
        sysParam.put("timestamp", timestamp);
        sysParam.put("appId", appId);
        sysParam.put("version", "1.0");
        sysParam.put("accessToken", accessToken);
        sysParam.put("busiSerial", "1");
        String entity = null;
        try {
            entity = SecurityUtils.encodeAES256HexUpper(busiParam, SecurityUtils.decodeHexUpper(key));
            String sign = SignUtil.sign(sysParam, entity, "HmacSHA256", key);
            return sign;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
