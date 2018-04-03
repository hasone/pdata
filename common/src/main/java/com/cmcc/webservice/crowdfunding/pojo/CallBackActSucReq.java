package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * CallBackActSucReq.java
 * 众筹成功通知，请求对象
 * @author wujiamin
 * @date 2017年2月9日
 */
@XStreamAlias("Request")
public class CallBackActSucReq {
    
    @XStreamAlias("Datetime")
    private String dateTime;
    
    @XStreamAlias("CallBackType")
    private String callBackType;
    
    @XStreamAlias("NotifyActRes")
    private CallBackActSucReqData data;
    
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

    public CallBackActSucReqData getData() {
        return data;
    }

    public void setData(CallBackActSucReqData data) {
        this.data = data;
    }
}
