package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 活动参数信息类（面向营销活动服务）
 * */
public class ActivityTemplate {
    private Long id;

    private String activityId;

    private Integer userType;

    private Integer givedNumber; //用户可中最大数量

    private Integer daily; //用户次数每天是否重置，0重置，1重置

    private Integer maxPlayNumber; //用户最大可玩数量

    private Integer checkType;

    private String checkUrl;

    private Integer fixedProbability;

    private String description;

    private String object;

    private String rules;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

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

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getGivedNumber() {
        return givedNumber;
    }

    public void setGivedNumber(Integer givedNumber) {
        this.givedNumber = givedNumber;
    }

    public Integer getDaily() {
        return daily;
    }

    public void setDaily(Integer daily) {
        this.daily = daily;
    }

    public Integer getMaxPlayNumber() {
        return maxPlayNumber;
    }

    public void setMaxPlayNumber(Integer maxPlayNumber) {
        this.maxPlayNumber = maxPlayNumber;
    }

    public Integer getCheckType() {
        return checkType;
    }

    public void setCheckType(Integer checkType) {
        this.checkType = checkType;
    }

    public String getCheckUrl() {
        return checkUrl;
    }

    public void setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl == null ? null : checkUrl.trim();
    }

    public Integer getFixedProbability() {
        return fixedProbability;
    }

    public void setFixedProbability(Integer fixedProbability) {
        this.fixedProbability = fixedProbability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object == null ? null : object.trim();
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules == null ? null : rules.trim();
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

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}