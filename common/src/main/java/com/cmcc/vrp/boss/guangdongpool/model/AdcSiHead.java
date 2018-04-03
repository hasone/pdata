package com.cmcc.vrp.boss.guangdongpool.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 请求和响应共有的头信息
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
public class AdcSiHead {
    @XStreamAlias("BizCode")
    private String bizCode;

    @XStreamAlias("TransID")
    private String transID;

    @XStreamAlias("ActionCode")
    private String actionCode;

    @XStreamAlias("TimeStamp")
    private String timeStamp;

    @XStreamAlias("SIAppID")
    private String siAppID;

    @XStreamAlias("TestFlag")
    private String testFlag;

    @XStreamAlias("Dealkind")
    private String dealkind;

    @XStreamAlias("Priority")
    private String priority;

    @XStreamAlias("Version")
    private String version;

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSiAppID() {
        return siAppID;
    }

    public void setSiAppID(String siAppID) {
        this.siAppID = siAppID;
    }

    public String getTestFlag() {
        return testFlag;
    }

    public void setTestFlag(String testFlag) {
        this.testFlag = testFlag;
    }

    public String getDealkind() {
        return dealkind;
    }

    public void setDealkind(String dealkind) {
        this.dealkind = dealkind;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
