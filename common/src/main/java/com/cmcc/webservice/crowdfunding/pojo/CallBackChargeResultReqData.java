package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * CallBackChargeResultReqData.java
 * @author wujiamin
 * @date 2017年4月27日
 */
@XStreamAlias("Record")
public class CallBackChargeResultReqData {
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
