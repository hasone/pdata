package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/13.
 */
@XStreamAlias("TransInfo")
public class TransInfoReq {
    @XStreamAlias("SessionID")
    private String sessionID;
    @XStreamAlias("TransIDO")
    private String transIDO;
    @XStreamAlias("TransIDOTime")
    private String transIDOTime;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getTransIDO() {
        return transIDO;
    }

    public void setTransIDO(String transIDO) {
        this.transIDO = transIDO;
    }

    public String getTransIDOTime() {
        return transIDOTime;
    }

    public void setTransIDOTime(String transIDOTime) {
        this.transIDOTime = transIDOTime;
    }
}
