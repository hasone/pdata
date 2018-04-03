package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Response")
public class ActivityResultResp {
    
    @XStreamAlias("Datetime")
    private String dateTime;
    
    @XStreamAlias("Code")
    private String code;
    
    @XStreamAlias("Message")
    private String message;
    
    @XStreamAlias("ActivityResult")
    private ActivityResultPojo activityResultPojo;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public ActivityResultPojo getActivityResultPojo() {
        return activityResultPojo;
    }

    public void setActivityResultPojo(ActivityResultPojo activityResultPojo) {
        this.activityResultPojo = activityResultPojo;
    }

}
