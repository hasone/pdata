package com.cmcc.vrp.boss.heilongjiang.fee;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:34:38
*/
public class Feature {
    @JsonProperty("group_no")
    private String groupNo;

    @JsonProperty("group_acc_no")
    private String groupAccNo;

    @JsonProperty("appkey")
    private String appKey;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getGroupAccNo() {
        return groupAccNo;
    }

    public void setGroupAccNo(String groupAccNo) {
        this.groupAccNo = groupAccNo;
    }

    @Override
    public String toString() {
        return "Feature [groupNo=" + groupNo + ", groupAccNo=" + groupAccNo + "]";
    }
}
