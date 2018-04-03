package com.cmcc.vrp.ec.service;

/**
 * 认证信息
 * <p>
 * Created by sunyiwei on 2016/9/23.
 */
public class AuthPojo {
    private String appKey;  //企业（平台）的appKey
    private String fingerprint;  //平台下某企业的fingerprint
    private String subAppKey;  //平台中企业在该平台的appkey
    private String subAppSecret;  //平台中企业在该平台的appSecret

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getSubAppKey() {
        return subAppKey;
    }

    public void setSubAppKey(String subAppKey) {
        this.subAppKey = subAppKey;
    }

    public String getSubAppSecret() {
        return subAppSecret;
    }

    public void setSubAppSecret(String subAppSecret) {
        this.subAppSecret = subAppSecret;
    }
}
