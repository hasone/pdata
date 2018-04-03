/**
 * @Title: PubInfo.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model
 * @author: qihang
 * @date: 2016年2月2日 上午8:51:15
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model;

/**
 * @ClassName: PubInfo
 * @Description: 公用报头类
 * @author: qihang
 * @date: 2016年2月2日 上午8:51:15
 *
 */
public class PubInfo {
    String transactionId;

    String transactionTime;

    String appId;

    String clientIP;

    String countyCode;

    String regionCode;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }


}
