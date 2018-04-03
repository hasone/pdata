package com.cmcc.vrp.province.module;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 四川单点登录请求响应对象
 * ScUserInfoResp.java
 * @author wujiamin
 * @date 2017年4月17日
 */
public class ScUserInfoResp {
    @JSONField(name="AppId")
    private String appId;
    
    @JSONField(name="Sign")
    private String sign;
    
    @JSONField(name="MsgKey")
    private String msgKey;
    
    @JSONField(name="RespCode")
    private String respCode;
    
    @JSONField(name="RespDesc")
    private String respDesc;
    
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

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
