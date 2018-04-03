package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * JoinActivityResp.java
 * 广东众筹活动报名接口，响应对象，业务参数
 * @author wujiamin
 * @date 2017年2月8日
 */
@XStreamAlias("JoinResult")
public class JoinActivityResultPojo { 
    @XStreamAlias("Mobile")
    String mobile;
    
    @XStreamAlias("RecordId")
    String recordId;
    
    @XStreamAlias("WinTime")
    String winTime;
    
    @XStreamAlias("PayResult")
    String payResult;
    
    @XStreamAlias("PrizeId")
    Long prizeId;

    public Long getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Long prizeId) {
        this.prizeId = prizeId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getWinTime() {
        return winTime;
    }

    public void setWinTime(String winTime) {
        this.winTime = winTime;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }
}
