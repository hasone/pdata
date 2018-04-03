package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * CallBackActSucReqData.java
 * 众筹成功通知，请求对象，
 * @author wujiamin
 * @date 2017年2月9日
 */
@XStreamAlias("NotifyActRes")
public class CallBackActSucReqData {
    
    @XStreamAlias("ActivityId")
    private String activityId;
    
    @XStreamAlias("ActivityName")
    private String activityName;
    
    @XStreamAlias("TargetCount")
    private Long targetCount;
    
    @XStreamAlias("CurrentCount")
    private Long currentCount;
    
    @XStreamAlias("Result")
    private String result;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }   
}
