package com.cmcc.vrp.boss.jiangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * @author lgk8023
 *
 */
@XStreamAlias("BODY")
public class JxQueryBodyReq {

    @XStreamAlias("ECCODE")
    private String ecCode;
    @XStreamAlias("PHONE")
    private String phone;
    @XStreamAlias("ORDER_SID")
    private String orderSid;
    @XStreamAlias("ORDER_TIMESTAMP")
    private String orderTimestamp;
    public String getEcCode() {
        return ecCode;
    }
    public void setEcCode(String ecCode) {
        this.ecCode = ecCode;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
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
    
}
