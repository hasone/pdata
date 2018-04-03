package com.cmcc.vrp.province.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;


/**
 * 活动核心基本信息类
 * */
public class Activities {
    private Long id;

    private String activityId;

    private Integer type;

    private Long entId;

    private String name;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Long creatorId;

    private Integer status;

    private Integer deleteFlag;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    //extended
    private Long prizeCount;
    private Long userCount;
    private Long productSize;//个人集中化平台使用，流量币赠送记录中赠送的个数
    private Integer download;//下载标记
    private String entName; //企业名称
    
    private Long currentCount;//当前参与人数
    private Long targetCount;//目标参与人数
    private Integer result;//众筹结果
    private Integer payResult;//用户支付结果（众筹）
    private String banner;
    private String logo;
    
    /**
     * 拓展activityCreator
     */
    private ActivityCreator activityCreator;

    //extended for guangdong crowd-funding
    //private Long currentCount; //众筹成功人数

    private Integer crowdFundingResult; //众筹成功标示

    //public Long getCurrentCount() {
    //    return currentCount;
    //}

    //public void setCurrentCount(Long currentCount) {
    //    this.currentCount = currentCount;
    //}

    public Integer getCrowdFundingResult() {
        return crowdFundingResult;
    }

    public void setCrowdFundingResult(Integer crowdFundingResult) {
        this.crowdFundingResult = crowdFundingResult;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    /**
     * @return the productSize
     */
    public Long getProductSize() {
        return productSize;
    }

    /**
     * @param productSize the productSize to set
     */
    public void setProductSize(Long productSize) {
        this.productSize = productSize;
    }

    /**
     * @return the userCount
     */
    public Long getUserCount() {
        return userCount;
    }

    /**
     * @param userCount the userCount to set
     */
    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public Long getPrizeCount() {
        return prizeCount;
    }

    public void setPrizeCount(Long prizeCount) {
        this.prizeCount = prizeCount;
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
        this.activityId = activityId == null ? null : activityId.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getDownload() {
        return download;
    }

    public void setDownload(Integer download) {
        this.download = download;
    }

    public Long getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(Long currentCount) {
        this.currentCount = currentCount;
    }

    public Long getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(Long targetCount) {
        this.targetCount = targetCount;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getPayResult() {
        return payResult;
    }

    public void setPayResult(Integer payResult) {
        this.payResult = payResult;
    }

    public ActivityCreator getActivityCreator() {
        return activityCreator;
    }

    public void setActivityCreator(ActivityCreator activityCreator) {
        this.activityCreator = activityCreator;
    }
    
    
}