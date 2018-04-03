package com.cmcc.vrp.boss.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by leelyn on 2016/8/17.
 */
@XStreamAlias("Request")
public class OrderReq {
    @XStreamAlias("TransID")
    private String transID;

    @XStreamAlias("UserDataList")
    private List<UserData> userDataList;

    @XStreamAlias("AppKey")
    private String appKey;

    @XStreamAlias("RequestTime")
    private String requestTime;

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public List<UserData> getUserDataList() {
        return userDataList;
    }

    public void setUserDataList(List<UserData> userDataList) {
        this.userDataList = userDataList;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}
