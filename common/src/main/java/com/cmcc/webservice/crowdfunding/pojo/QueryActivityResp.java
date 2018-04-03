package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * QueryActivityResp.java
 * 广东众筹活动查询接口，响应对象
 * @author wujiamin
 * @date 2017年2月8日
 */
@XStreamAlias("Response")
public class QueryActivityResp {    
    @XStreamAlias("Datetime")
    String responseTime;
    
    @XStreamAlias("Code")
    String code;
    
    @XStreamAlias("Message")
    String message;
    
    @XStreamAlias("Activity")
    ActivityPojo activityPojo;

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

    public ActivityPojo getActivityPojo() {
        return activityPojo;
    }

    public void setActivityPojo(ActivityPojo activityPojo) {
        this.activityPojo = activityPojo;
    }

}
