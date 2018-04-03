package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("Response")
public class SyncProResp {
    @XStreamAlias("Datetime")
    String responseTime;

    @XStreamAlias("SyncResult")
    SyncProRespData syncProRespData;

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public SyncProRespData getSyncProRespData() {
        return syncProRespData;
    }

    public void setSyncProRespData(SyncProRespData syncProRespData) {
        this.syncProRespData = syncProRespData;
    }
}
