package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/20.
 */
@XStreamAlias("Response")
public class ChargeResp {

    @XStreamAlias("Datetime")
    String responseTime;

    @XStreamAlias("ChargeData")
    ChargeRespData respData;

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public ChargeRespData getRespData() {
        return respData;
    }

    public void setRespData(ChargeRespData respData) {
        this.respData = respData;
    }
}
