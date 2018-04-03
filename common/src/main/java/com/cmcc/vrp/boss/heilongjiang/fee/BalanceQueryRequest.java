package com.cmcc.vrp.boss.heilongjiang.fee;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:31:46
*/
public class BalanceQueryRequest {

    private String appKey;
    private Date timestamp;
    private String phoneNo;

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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString() {
        return "BalanceQueryRequest [appKey=" + appKey + ", timestamp=" + timestamp + ", phoneNo=" + phoneNo + "]";
    }
}
