package com.cmcc.vrp.boss.henan.model;

/**
 * Created by leelyn on 2016/6/24.
 */
public class HaAuthResp {

    String access_token;
    String expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }
}
