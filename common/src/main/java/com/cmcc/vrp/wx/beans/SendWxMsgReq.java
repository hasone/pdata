package com.cmcc.vrp.wx.beans;

import java.util.Map;

/**
 * Created by leelyn on 2017/1/9.
 */
public class SendWxMsgReq {

    private String mobile;

    private Integer templateMsgType;

    private Long activityId;

    private Map<String, String> params;

    private String authentication;

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getTemplateMsgType() {
        return templateMsgType;
    }

    public void setTemplateMsgType(Integer templateMsgType) {
        this.templateMsgType = templateMsgType;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
