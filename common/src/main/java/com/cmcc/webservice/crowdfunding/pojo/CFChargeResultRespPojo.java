package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CFChargeResult")
public class CFChargeResultRespPojo {
    
    @XStreamAlias("Status")
    private String status;
    
    @XStreamAlias("Reason")
    private String reason;
    
    @XStreamAlias("ChargeTime")
    private String chargeTime;
    
    @XStreamAlias("Mobile")
    private String mobile;
    
    @XStreamAlias("ActivityId")
    private String activityId;
    
    @XStreamAlias("ActivityName")
    private String activityName;
    
    @XStreamAlias("PrizeName")
    private String prizeName;
    
    @XStreamAlias("ProductName")
    private String productName;
    
    @XStreamAlias("ProductCode")
    private String productCode;
    
    @XStreamAlias("Discount")
    private String Discount;
    
    @XStreamAlias("SerialNum")
    private String setialNum; //EC充值流水号
    
    @XStreamAlias("SystemNum")
    private String systemNum; //平台充值流水号，也是中奖纪录uuid

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(String chargeTime) {
        this.chargeTime = chargeTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getSetialNum() {
        return setialNum;
    }

    public void setSetialNum(String setialNum) {
        this.setialNum = setialNum;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }

}
