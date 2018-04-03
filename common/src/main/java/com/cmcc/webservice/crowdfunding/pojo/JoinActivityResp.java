package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * JoinActivityResp.java
 * 广东众筹活动报名接口，响应对象
 * @author wujiamin
 * @date 2017年2月8日
 */
public class JoinActivityResp {
    @XStreamAlias("Code")
    String code;
    
    @XStreamAlias("Message")
    String message;
    
    @XStreamAlias("JoinResult")
    JoinActivityResultPojo joinResultData;

    @XStreamAlias("Datetime")
    String responseTime;

    public JoinActivityResultPojo getJoinResultData() {
        return joinResultData;
    }

    public void setJoinResultData(JoinActivityResultPojo joinResultData) {
        this.joinResultData = joinResultData;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }
    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
