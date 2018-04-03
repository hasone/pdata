package com.cmcc.vrp.province.module;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 四川和生活单点登录请求data部分
 * ScUserInfoReqData.java
 * @author wujiamin
 * @date 2017年4月17日
 */
public class ScUserInfoReqData {
    @JSONField(name="TimeStamp")
    private String timeStamp;
    
    @JSONField(name="Token")
    private String token;
    
    @JSONField(name="UID")
    private String uid;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }  
}
