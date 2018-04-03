package com.cmcc.vrp.boss.bjym.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunyiwei on 2017/4/12.
 */
public class BjymCallbackRequestMessage {
    @SerializedName("mobiles")
    private String mobile;

    private String userPackage;

    private String recvTime;

    private String state;

    private String sendID;

    @SerializedName("statedes")
    private String stateDes;

    private String requestid;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserPackage() {
        return userPackage;
    }

    public void setUserPackage(String userPackage) {
        this.userPackage = userPackage;
    }

    public String getRecvTime() {
        return recvTime;
    }

    public void setRecvTime(String recvTime) {
        this.recvTime = recvTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSendID() {
        return sendID;
    }

    public void setSendID(String sendID) {
        this.sendID = sendID;
    }

    public String getStateDes() {
        return stateDes;
    }

    public void setStateDes(String stateDes) {
        this.stateDes = stateDes;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }
}
