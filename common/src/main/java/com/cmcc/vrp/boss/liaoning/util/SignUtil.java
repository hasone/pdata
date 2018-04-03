package com.cmcc.vrp.boss.liaoning.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月18日 下午8:02:59
*/
public class SignUtil{
    private static transient Log log = LogFactory.getLog(SignUtil.class);

    /**
     * @param sysParams
     * @param busiParams
     * @param protocol
     * @param key
     * @return
     * @throws Exception
     */
    public static String execute(Map<String, String> sysParams, String busiParams, String protocol, String key, String url) throws Exception {
        sysParams.put("isImportAppKey", "true");
        sysParams.put("appKey", key);
        return execute(sysParams, busiParams, protocol, key, url, 0);
    }
 
    private static String execute(Map<String, String> sysParams, String busiParams, String protocol, String key, String url, int retryNum)
        throws Exception{
        String sign = generateSign(sysParams, busiParams);
        sysParams.put("sign", sign);
 
 
        boolean isImportKey = sysParams.containsKey("isImportAppKey");
        if (isImportKey) {
            sysParams.remove("isImportAppKey");
            sysParams.remove("appKey");
        }
        String response = null;
        if ("http".equalsIgnoreCase(protocol)){
            response = HttpRequestHelper.request(sysParams, busiParams, url);
        }  else {
            throw new Exception("暂不支持此协议！");
        }
 
        if ((StringUtils.isNotBlank(response)) && (!(isImportKey))) {
            String respCode = ParseUtil.parseRespCode(response, (String)sysParams.get("format"));
            if (("20221".equals(respCode)) && 
                    (retryNum < 3)) {
                if (log.isDebugEnabled()) {
                    log.debug("签名验证失败，且重试次数未达到3次（当前" + retryNum + 
                            "次），获取最新的密钥重新发起调用！");
                }
                return execute(sysParams, busiParams, protocol, url, key, retryNum + 1);
            }
        }
 
        return response;
    }
    
    /**
     * @param sysParams
     * @param busiParams
     * @return
     */
    public static String generateSign(Map<String, String> sysParams, String busiParams) {
        Map paramsMap = new HashMap();
        paramsMap.putAll(sysParams);
        paramsMap.put("content", busiParams);
        try {
            String sign = SignEngineFactory.getSignEngine().generateSign(paramsMap);
            if (log.isDebugEnabled()) {
                log.debug("根据请求报文生成的sign：" + sign);
            }
            return sign;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
