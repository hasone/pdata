
package com.cmcc.vrp.boss.liaoning.util;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.asiainfo.openplatform.sign.ISignEngine;
import com.asiainfo.openplatform.utils.SecurityUtils;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月19日 上午8:38:16
*/
public class SHASignEngineImpl implements ISignEngine {
    private static SHASignEngineImpl instance;

    public static SHASignEngineImpl getSingleton() {
        if (instance == null) {
            synchronized (SHASignEngineImpl.class) {
                if (instance == null) {
                    instance = new SHASignEngineImpl();
                }
            }
        }

        return instance;
    }

    /**
     * (non-Javadoc)
     * @see com.asiainfo.openplatform.sign.ISignEngine#generateSign(java.util.Map)
     */
    public String generateSign(Map<String, String> paramsMap) throws Exception {
        String appKey = null;
        if (paramsMap.containsKey("isImportAppKey")) {
            paramsMap.remove("isImportAppKey");
            appKey = (String) paramsMap.remove("appKey");
        }

        String[] paramArr = (String[]) paramsMap.keySet().toArray(new String[paramsMap.size()]);
        Arrays.sort(paramArr);
        StringBuilder keyBuf = new StringBuilder();
        StringBuilder buf = new StringBuilder();
        buf.append(appKey);
        for (String param : paramArr) {
            if (!("sign".equals(param))) {
                String value = (String) paramsMap.get(param.trim());
                if (StringUtils.isNotBlank(value)) {
                    keyBuf.append(param).append("|");
                    buf.append(param).append(value.trim());
                }
            }
        }
        buf.append(appKey);

        String signStr = "";
        if (buf.length() > 0) {
            signStr = SecurityUtils.encodeHmacSHA256HexUpper(buf.toString(), SecurityUtils.decodeHexUpper(appKey));
        }

        return signStr;
    }
}