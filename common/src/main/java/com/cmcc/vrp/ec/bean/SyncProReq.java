package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("Request")
public class SyncProReq {
	
    @XStreamAlias("Datetime")
    String requestTime;

    @XStreamAlias("SyncInfo")
    SyncProReqData syncProReqData;

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public SyncProReqData getSyncProReqData() {
        return syncProReqData;
    }

    public void setSyncProReqData(SyncProReqData syncProReqData) {
        this.syncProReqData = syncProReqData;
    }

}
