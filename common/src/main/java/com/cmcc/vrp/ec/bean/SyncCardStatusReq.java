package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("Request")
public class SyncCardStatusReq {
    
    @XStreamAlias("Datetime")
    String requestTime;

    @XStreamAlias("SyncCardStatus")
    SyncCardStatusReqData syncCardStatusReqData;

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public SyncCardStatusReqData getSyncCardStatusReqData() {
        return syncCardStatusReqData;
    }

    public void setSyncCardStatusReqData(SyncCardStatusReqData syncCardStatusReqData) {
        this.syncCardStatusReqData = syncCardStatusReqData;
    }

}
