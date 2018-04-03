package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * CallBackChargeResultReq.java
 * @author wujiamin
 * @date 2017年4月27日
 */
@XStreamAlias("Request")
public class CallBackChargeResultReq {
    
    @XStreamAlias("Datetime")
    private String dateTime;
    
    @XStreamAlias("CallBackType")
    private String callBackType;
    
    @XStreamAlias("Record")
    private CallBackChargeResultReqData data;
    
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

    public CallBackChargeResultReqData getData() {
        return data;
    }

    public void setData(CallBackChargeResultReqData data) {
        this.data = data;
    }
}
