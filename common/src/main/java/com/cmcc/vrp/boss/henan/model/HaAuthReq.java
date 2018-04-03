package com.cmcc.vrp.boss.henan.model;

/**
 * Created by leelyn on 2016/6/24.
 */
public class HaAuthReq {

    String appId;
    String appKey;
    String grantType;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
