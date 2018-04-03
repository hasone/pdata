package com.cmcc.vrp.ec.bean.individual;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * IndividualRedpacketReq.java
 * @author wujiamin
 * @date 2017年1月13日
 */
@XStreamAlias("CreateResponse")
public class IndividualRedpacketResp {
    @XStreamAlias("ActivityId")
    private String activityId;
    
    @XStreamAlias("ActivityUrl")
    private String activityUrl;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }
}
