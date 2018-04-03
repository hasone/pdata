package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ActivityResult")
public class ActivityResultPojo {
    
    @XStreamAlias("ActivityId")
    private String activityId;
    
    @XStreamAlias("ActivityName")
    private String activityName;
    
    @XStreamAlias("Result")
    private int result;
    
    @XStreamAlias("TargetCount")
    private Long targetCount;
    
    @XStreamAlias("CurrentCount")
    private Long currentCount;

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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Long getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(Long targetCount) {
        this.targetCount = targetCount;
    }

    public Long getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(Long currentCount) {
        this.currentCount = currentCount;
    }

}
