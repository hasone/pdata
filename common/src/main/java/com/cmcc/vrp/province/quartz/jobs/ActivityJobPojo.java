package com.cmcc.vrp.province.quartz.jobs;

import java.io.Serializable;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午4:03:02
*/
public class ActivityJobPojo implements Serializable {

    private static final long serialVersionUID = 276261984834630857L;

    private String activityId;//活动ID

    public ActivityJobPojo() {
    }

    public ActivityJobPojo(String activityId) {
        this.activityId = activityId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
