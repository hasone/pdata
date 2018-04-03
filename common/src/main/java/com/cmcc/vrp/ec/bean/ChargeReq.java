package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/20.
 */
@XStreamAlias("Request")
public class ChargeReq {

    @XStreamAlias("Datetime")
    String requestTime;

    @XStreamAlias("ChargeData")
    ChargeReqData chargeReqData;

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public ChargeReqData getChargeReqData() {
        return chargeReqData;
    }

    public void setChargeReqData(ChargeReqData chargeReqData) {
        this.chargeReqData = chargeReqData;
    }
}
