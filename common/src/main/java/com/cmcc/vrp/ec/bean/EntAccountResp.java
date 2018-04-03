package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 * 2016年11月11日
 *
 */
@XStreamAlias("Response")
public class EntAccountResp {
	
    @XStreamAlias("Datetime")
    String responseTime;

    @XStreamAlias("EntInfo")
    EntInfoRespData entInfoRespData;


    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public EntInfoRespData getEntInfoRespData() {
        return entInfoRespData;
    }

    public void setEntInfoRespData(EntInfoRespData entInfoRespData) {
        this.entInfoRespData = entInfoRespData;
    }
}
