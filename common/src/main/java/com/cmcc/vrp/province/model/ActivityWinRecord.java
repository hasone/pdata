package com.cmcc.vrp.province.model;

import java.util.Date;


/**
 * 营销活动中奖信息
 * */
public class ActivityWinRecord {
    private Long id;

    private String recordId;

    private String activityId;

    private String ownMobile;

    private String chargeMobile;

    private Long prizeId;

    private String isp;

    private Date winTime;

    private Date chargeTime;

    private Integer status;

    private String reason;

    private Integer deleteFlag;

    private Date createTime;

    private Date updateTime;

    private String size; //奖品大小

    private String statusCode; //充值状态码
    
    private Integer payResult;
    
    private String wxOpenid;
    

    private String paySerialNum;//支付记录的流水号

    private String channel; //充值渠道，目前用于上海流量


    //extend
    private Long productSize;

    private Long price;

    private String productName;

    private String entName;

    private String activityName;

    private Date activityStartTime;

    private Date activityEndTime;

    private Long flowcoinSize;  //流量币大小
    
    private Integer discount; //奖品折扣，众筹活动有该字段
    
    private String prizeName; //奖品名称
    
    private String logo; //企业logo名称，众筹活动有该字段
    
    private String banner; //企业banner名称，众筹活动有该字段
    
    private Date paymentTime; //订单支付时间
    
    private String orderMobile;//四川红包订购用户
    
    private String crowdfundingStatus;//会员列表众筹活动状态

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Long getFlowcoinSize() {
        return flowcoinSize;
    }

    public void setFlowcoinSize(Long flowcoinSize) {
        this.flowcoinSize = flowcoinSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getOwnMobile() {
        return ownMobile;
    }

    public void setOwnMobile(String ownMobile) {
        this.ownMobile = ownMobile == null ? null : ownMobile.trim();
    }

    public String getChargeMobile() {
        return chargeMobile;
    }

    public void setChargeMobile(String chargeMobile) {
        this.chargeMobile = chargeMobile == null ? null : chargeMobile.trim();
    }

    public Long getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Long prizeId) {
        this.prizeId = prizeId;
    }

    public Date getWinTime() {
        return winTime;
    }

    public void setWinTime(Date winTime) {
        this.winTime = winTime;
    }

    public Date getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Date chargeTime) {
        this.chargeTime = chargeTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getProductSize() {
        return productSize;
    }

    public void setProductSize(Long productSize) {
        this.productSize = productSize;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(Date activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public Date getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(Date activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWxOpenid() {
        return wxOpenid;
    }

    public void setWxOpenid(String wxOpenid) {
        this.wxOpenid = wxOpenid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getPayResult() {
        return payResult;
    }

    public void setPayResult(Integer payResult) {
        this.payResult = payResult;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getOrderMobile() {
        return orderMobile;
    }

    public void setOrderMobile(String orderMobile) {
        this.orderMobile = orderMobile;
    }
    
    public String getCrowdfundingStatus() {
        return crowdfundingStatus;
    }

    public void setCrowdfundingStatus(String crowdfundingStatus) {
        this.crowdfundingStatus = crowdfundingStatus;
    }

    public String getPaySerialNum() {
        return paySerialNum;
    }

    public void setPaySerialNum(String paySerialNum) {
        this.paySerialNum = paySerialNum;
    }
    
}
