package com.cmcc.vrp.boss.jiangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("BODY")
public class JxQueryBodyRep {
    @XStreamAlias("RESULTCODE")
    private String resultCode;
    
    @XStreamAlias("RESULTMSG")
    private String resultMsg;
    
    @XStreamAlias("PHONE")  
    private String phone;
    
    @XStreamAlias("VOLUME")
    private String volume;
    
    @XStreamAlias("ORDER_SID")
    private String orderSid;
    
    @XStreamAlias("ORDER_TIMESTAMP")
    private String orderTimestamp;
    
    @XStreamAlias("DEALCODE")
    private String dealCode;
    
    @XStreamAlias("DEALMSG")
    private String dealMsg;
    
    @XStreamAlias("DEALTIME")
    private String dealTime;
    
    @XStreamAlias("STATUS")
    private String status;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getOrderSid() {
        return orderSid;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
    }

    public String getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(String orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public String getDealCode() {
        return dealCode;
    }

    public void setDealCode(String dealCode) {
        this.dealCode = dealCode;
    }

    public String getDealMsg() {
        return dealMsg;
    }

    public void setDealMsg(String dealMsg) {
        this.dealMsg = dealMsg;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
