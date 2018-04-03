/**
 * 
 */
package com.cmcc.vrp.boss.sichuan.model.individual;


/**
 * 四川boss查询套餐内流量余额请求参数
 * @author wujiamin
 * @date 2016年11月1日
 */
public class ScAppQryRequest {
    private String appKey;
    private String timeStamp;
    private String userName;
    private String phoneNo;
    private String qryType;
    private String yearMonth;
    private String loginNo;
    private String sign;
    public String getAppKey() {
        return appKey;
    }
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    public String getQryType() {
        return qryType;
    }
    public void setQryType(String qryType) {
        this.qryType = qryType;
    }
    public String getYearMonth() {
        return yearMonth;
    }
    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }
    public String getLoginNo() {
        return loginNo;
    }
    public void setLoginNo(String loginNo) {
        this.loginNo = loginNo;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }    
}
