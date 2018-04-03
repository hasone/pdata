package com.cmcc.vrp.boss.beijing.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/26.
 */
@XStreamAlias("Header")
public class OrderRespHeader {

    @XStreamAlias("TransactionID")
    String transactionId;

    @XStreamAlias("ResponseTime")
    String responseTime;

    @XStreamAlias("RetCode")
    Integer retCode;

    @XStreamAlias("RetDesc")
    String retDesc;

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public String getRetDesc() {
        return retDesc;
    }

    public void setRetDesc(String retDesc) {
        this.retDesc = retDesc;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }
}
