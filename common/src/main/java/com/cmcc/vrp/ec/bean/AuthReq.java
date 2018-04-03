package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/18.
 */
@XStreamAlias("Request")
public class AuthReq {
    @XStreamAlias("Authorization")
    AuthReqData authorization;

    @XStreamAlias("Datetime")
    String requestTime;

    public AuthReqData getAuthorization() {
        return authorization;
    }

    public void setAuthorization(AuthReqData authorization) {
        this.authorization = authorization;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}
