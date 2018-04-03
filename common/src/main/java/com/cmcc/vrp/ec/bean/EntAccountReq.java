package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 * 2016/11/09
 */
@XStreamAlias("Request")
public class EntAccountReq {
	
    @XStreamAlias("Datetime")
    String requestTime;

    @XStreamAlias("EntInfo")
    EntInfoReqData entInfoReqData;

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public EntInfoReqData getEntInfoReqData() {
        return entInfoReqData;
    }

    public void setEntInfoReqData(EntInfoReqData entInfoReqData) {
        this.entInfoReqData = entInfoReqData;
    }

}
