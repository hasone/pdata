package com.cmcc.vrp.ec.bean.individual;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * RedpacketCallBackReqData.java
 * @author wujiamin
 * @date 2017年1月13日
 */
public class RedpacketCallBackReqData {

    @XStreamAlias("ActivityId")
    String activityId;

    @XStreamAlias("Mobile")
    String mobile;

    @XStreamAlias("Size")
    String size;

    @XStreamAlias("WinTime")
    private String winTime;

    @XStreamAlias("RecordId")
    private String recordId;

    @XStreamAlias("ChargeTime")
    private String chargeTime;
    
    @XStreamAlias("ChargeStatus")
    private Integer chargeStatus;
    
    @XStreamAlias("ChargeMsg")
    private String chargeMsg;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWinTime() {
        return winTime;
    }

    public void setWinTime(String winTime) {
        this.winTime = winTime;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(String chargeTime) {
        this.chargeTime = chargeTime;
    }

    public Integer getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Integer chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getChargeMsg() {
        return chargeMsg;
    }

    public void setChargeMsg(String chargeMsg) {
        this.chargeMsg = chargeMsg;
    }
}
