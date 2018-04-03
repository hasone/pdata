package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/24.
 */

public class CallBackReqData {

    @XStreamAlias("SerialNum")
    String ecSerialNum;

    @XStreamAlias("SystemNum")
    String systemNum;

    @XStreamAlias("Status")
    Integer status;

    @XStreamAlias("Mobile")
    private String mobile;

    @XStreamAlias("Description")
    private String description;

    @XStreamAlias("ChargeTime")
    private String chargeTime;

    public String getEcSerialNum() {
        return ecSerialNum;
    }

    public void setEcSerialNum(String ecSerialNum) {
        this.ecSerialNum = ecSerialNum;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(String chargeTime) {
        this.chargeTime = chargeTime;
    }
}
