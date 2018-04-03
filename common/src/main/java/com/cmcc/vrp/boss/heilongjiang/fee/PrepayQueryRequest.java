package com.cmcc.vrp.boss.heilongjiang.fee;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午11:25:00
*/
public class PrepayQueryRequest {
    private String appKey;
    private Date timestamp;
    private String groupAccNo;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getGroupAccNo() {
        return groupAccNo;
    }

    public void setGroupAccNo(String groupAccNo) {
        this.groupAccNo = groupAccNo;
    }

    @Override
    public String toString() {
        return "PrepayQueryRequest [appKey=" + appKey + ", timestamp=" + timestamp + ", groupAccNo=" + groupAccNo + "]";
    }
}
