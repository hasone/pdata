package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/18.
 */
@XStreamAlias("TransInfo")
public class TransInfoResp {

    @XStreamAlias("SessionID")
    private String sessionID;
    @XStreamAlias("TransIDO")
    private String transIDO;
    @XStreamAlias("TransIDOTime")
    private String transIDOTime;
    @XStreamAlias("TransIDH")
    private String transIDH;
    @XStreamAlias("TransIDHTime")
    private String transIDHTime;

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

    public String getTransIDH() {
        return transIDH;
    }

    public void setTransIDH(String transIDH) {
        this.transIDH = transIDH;
    }

    public String getTransIDHTime() {
        return transIDHTime;
    }

    public void setTransIDHTime(String transIDHTime) {
        this.transIDHTime = transIDHTime;
    }
}
