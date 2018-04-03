package com.cmcc.vrp.boss.sichuan.model;

import org.apache.commons.lang.StringUtils;

/**
 * ScQryRandPassRequest.java
 * @author wujiamin
 * @date 2017年3月17日
 */
public class ScQryRandPassRequest {
    /**
     * 商家APPKEY
     */
    private String appKey;
    
    /**
     * 时间戳
     */
    private String timeStamp;
    
    /**
     * 登录帐号
     */
    private String userName;
    
    /**
     * 手机号码
     */
    private String phoneNo;
    
    /**
     * 操作类型
     */
    private String passMode;
    
    /**
     * 用户输入的密码
     */
    private String smsPwd;
    
    /**
     * 密码长度
     */
    private String passLength;
    
    /**
     * 操作工号
     */
    private String loginNo;

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

    public String getPassMode() {
        return passMode;
    }

    public void setPassMode(String passMode) {
        this.passMode = passMode;
    }

    public String getSmsPwd() {
        return smsPwd;
    }

    public void setSmsPwd(String smsPwd) {
        this.smsPwd = smsPwd;
    }

    public String getPassLength() {
        return passLength;
    }

    public void setPassLength(String passLength) {
        this.passLength = passLength;
    }

    public String getLoginNo() {
        return loginNo;
    }

    public void setLoginNo(String loginNo) {
        this.loginNo = loginNo;
    }
    

    /**
     * 得到GET请求?后的参数,为空的不填充
     */
    public String getReqParams(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.isBlank(appKey) ? "" : "&appKey=" + appKey);
        buffer.append(StringUtils.isBlank(loginNo) ? "" : "&login_no=" + loginNo);
        buffer.append(StringUtils.isBlank(passLength) ? "" : "&passLength=" + passLength);
        buffer.append(StringUtils.isBlank(passMode) ? "" : "&passMode=" + passMode);
        buffer.append(StringUtils.isBlank(phoneNo) ? "" : "&phone_no=" + phoneNo);
        buffer.append(StringUtils.isBlank(smsPwd) ? "" : "&smsPwd=" + smsPwd);
        buffer.append(StringUtils.isBlank(timeStamp) ? "" : "&timeStamp=" + timeStamp);
        buffer.append(StringUtils.isBlank(userName) ? "" : "&userName=" + userName);
        String reqStr = buffer.toString();
        return reqStr.length()>0 ? reqStr.substring(1) : reqStr;     
    }
}
