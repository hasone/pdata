package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by sunyiwei on 2016/7/6.
 */
@XStreamAlias("Request")
public class CallBackReq {
    @XStreamAlias("Datetime")
    private String dateTime;

    @XStreamAlias("Record")
    private CallBackReqData callBackReqData;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public CallBackReqData getCallBackReqData() {
        return callBackReqData;
    }

    public void setCallBackReqData(CallBackReqData callBackReqData) {
        this.callBackReqData = callBackReqData;
    }
}
