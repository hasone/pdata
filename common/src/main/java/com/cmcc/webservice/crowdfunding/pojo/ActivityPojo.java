package com.cmcc.webservice.crowdfunding.pojo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("NotifyActivity")
public class ActivityPojo {
    
    @XStreamAlias("ActivityId")
    private String activityId;
    
    @XStreamAlias("ActivityName")
    private String activityName;
    
    @XStreamAlias("Status")
    private String status;
    
    @XStreamAlias("StartTime")
    private String startTime;
    
    @XStreamAlias("EndTime")
    private String endTime;
    
    @XStreamAlias("ChargeType")
    private String chargeType;
    
    @XStreamAlias("HasWhiteOrBlack")
    private String hasWhiteOrBlack;
    
    @XStreamAlias("TargetCount")
    private String targetCount;
    
    @XStreamAlias("CurrentCount")
    private String currentCount;
    
    @XStreamAlias("Result")
    private String result;
    
    @XStreamAlias("Rules")
    private String rules;
    
    @XStreamAlias("joinType")
    private String joinType;
    
    @XStreamAlias("Prizes")
    private List<PrizePojo> prizes;
    
    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getHasWhiteOrBlack() {
        return hasWhiteOrBlack;
    }

    public void setHasWhiteOrBlack(String hasWhiteOrBlack) {
        this.hasWhiteOrBlack = hasWhiteOrBlack;
    }

    public String getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(String targetCount) {
        this.targetCount = targetCount;
    }

    public String getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(String currentCount) {
        this.currentCount = currentCount;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
    
    public List<PrizePojo> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<PrizePojo> prizes) {
        this.prizes = prizes;
    }

}
