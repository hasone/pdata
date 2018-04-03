package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 广东众筹报名结果查询，响应对象
 * QueryJoinResResp.java
 * @author wujiamin
 * @date 2017年2月9日
 */
@XStreamAlias("Response")
public class QueryJoinResResp {
    @XStreamAlias("Datetime")
    String responseTime;
    
    @XStreamAlias("Code")
    String code;
    
    @XStreamAlias("Message")
    String message;
    
    @XStreamAlias("JoinResult")
    JoinActivityResultPojo data;

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

    public JoinActivityResultPojo getData() {
        return data;
    }

    public void setData(JoinActivityResultPojo data) {
        this.data = data;
    }
}
