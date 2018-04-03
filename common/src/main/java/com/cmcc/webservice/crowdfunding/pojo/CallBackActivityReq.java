package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by qinqinyan on 2017/2/8.
 */
@XStreamAlias("Request")
public class CallBackActivityReq {
    
    @XStreamAlias("Datetime")
    private String dateTime;
    
    @XStreamAlias("CallBackType")
    private String callBackType;
    
    @XStreamAlias("NotifyActivity")
    private ActivityPojo acitityPojo;
    
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCallBackType() {
        return callBackType;
    }

    public void setCallBackType(String callBackType) {
        this.callBackType = callBackType;
    }

    public ActivityPojo getAcitityPojo() {
        return acitityPojo;
    }

    public void setAcitityPojo(ActivityPojo acitityPojo) {
        this.acitityPojo = acitityPojo;
    }

    
}
