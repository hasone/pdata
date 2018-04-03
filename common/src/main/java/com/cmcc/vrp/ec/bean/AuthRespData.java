package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/25.
 */
@XStreamAlias("Authorization")
public class AuthRespData {

    @XStreamAlias("Token")
    String token;

    @XStreamAlias("ExpiredTime")
    String expiredTime;

    @XStreamAlias("CreatedTime")
    String createdTime;

    @XStreamAlias("fingerprint")
    String fingerprint;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}
