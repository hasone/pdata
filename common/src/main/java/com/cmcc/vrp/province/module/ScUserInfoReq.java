package com.cmcc.vrp.province.module;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 四川和生活单点登录请求
 * ScUserInfoReq.java
 * @author wujiamin
 * @date 2017年4月17日
 */
public class ScUserInfoReq {
    @JSONField(name="AppId")
    private String appId;
    
    @JSONField(name="Sign")
    private String sign;
    
    @JSONField(name="MsgKey")
    private String msgKey;
    
    @JSONField(name="PartnerId")
    private String partnerId;
    
    @JSONField(name="Data")
    private String data;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
