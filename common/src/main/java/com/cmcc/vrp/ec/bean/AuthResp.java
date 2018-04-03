package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/18.
 */
@XStreamAlias("Response")
public class AuthResp {

    @XStreamAlias("Datetime")
    String responseTime;

    @XStreamAlias("Authorization")
    AuthRespData authRespData;

    public AuthRespData getAuthRespData() {
        return authRespData;
    }

    public void setAuthRespData(AuthRespData authRespData) {
        this.authRespData = authRespData;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

}
