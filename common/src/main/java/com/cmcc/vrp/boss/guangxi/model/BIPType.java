package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/13.
 */
@XStreamAlias("BIPType")
public class BIPType {

    @XStreamAlias("BIPCode")
    private String bIPCode;
    @XStreamAlias("ActivityCode")
    private String activityCode;
    @XStreamAlias("ActionCode")
    private String actionCode;

    /**
     * @return
     */
    public String getbIPCode() {
        return bIPCode;
    }

    /**
     * @param bIPCode
     */
    public void setbIPCode(String bIPCode) {
        this.bIPCode = bIPCode;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
}
