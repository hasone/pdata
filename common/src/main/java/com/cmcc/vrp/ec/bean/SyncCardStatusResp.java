package com.cmcc.vrp.ec.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月10日 下午4:52:43
*/
@XStreamAlias("Response")
public class SyncCardStatusResp {
    @XStreamAlias("Datetime")
    String responseTime;

    @XStreamAlias("SyncResults")
    List<SyncCardStatusRespData> syncCardStatusRespData;


    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public List<SyncCardStatusRespData> getSyncCardStatusRespData() {
        return syncCardStatusRespData;
    }

    public void setSyncCardStatusRespData(List<SyncCardStatusRespData> syncCardStatusRespData) {
        this.syncCardStatusRespData = syncCardStatusRespData;
    }

}
