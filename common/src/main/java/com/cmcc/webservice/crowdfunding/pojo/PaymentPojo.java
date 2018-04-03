package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Payment")
public class PaymentPojo {
    
    @XStreamAlias("Mobile")
    private String mobile;
    
    @XStreamAlias("SerialNum")
    private String serialNum;
    
    @XStreamAlias("ActivityId")
    private String activityId;
    
    @XStreamAlias("PrizeId")
    private String prizeId;
    
    @XStreamAlias("EnterpriseCode")
    private String enterpriseCode;
    
    @XStreamAlias("EcProductCode")
    String ecProductCode;

    public String getEcProductCode() {
        return ecProductCode;
    }

    public void setEcProductCode(String ecProductCode) {
        this.ecProductCode = ecProductCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(String prizeId) {
        this.prizeId = prizeId;
    }

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }
}
