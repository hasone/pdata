package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/25.
 */
@XStreamAlias("Authorization")
public class AuthReqData {

    @XStreamAlias("AppKey")
    String appKey;

    @XStreamAlias("Sign")
    String sign;

    @XStreamAlias("AppSecret")
    String appSecret;

    @XStreamAlias("SecInterface")
    String secInterface;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSecInterface() {
        return secInterface;
    }

    public void setSecInterface(String secInterface) {
        this.secInterface = secInterface;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }


}
