package com.cmcc.vrp.boss.bjym.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 北京云漫的请求体
 *
 * Created by sunyiwei on 2017/4/6.
 */
public class BjymRequestBody {
    private final String type = "0";

    @SerializedName("requestid")
    private String requestId;

    private int size;

    @SerializedName("userdataList")
    private List<BjymUserData> userDataList;

    public String getType() {
        return type;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<BjymUserData> getUserDataList() {
        return userDataList;
    }

    public void setUserDataList(List<BjymUserData> userDataList) {
        this.userDataList = userDataList;
    }
}
