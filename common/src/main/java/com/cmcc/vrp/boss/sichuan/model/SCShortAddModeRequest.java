package com.cmcc.vrp.boss.sichuan.model;

/**
 * 附加资费办理请求（sShortAddMode）
 *
 * @author wujiamin
 * @date 2016年9月20日下午2:15:22
 */
public class SCShortAddModeRequest {
    private String appKey;

    private String timeStamp;

    private String userName;

    private String phoneNo;// 11位四川移动手机号码

    private String operateType;// 操作类型：A订购，D退订

    private String prodPrcid;// 产品资费标识

    private String limitCode;// 限制代码

    private String phoneSlv;// 情侣号码

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getProdPrcid() {
        return prodPrcid;
    }

    public void setProdPrcid(String prodPrcid) {
        this.prodPrcid = prodPrcid;
    }

    public String getLimitCode() {
        return limitCode;
    }

    public void setLimitCode(String limitCode) {
        this.limitCode = limitCode;
    }

    public String getPhoneSlv() {
        return phoneSlv;
    }

    public void setPhoneSlv(String phoneSlv) {
        this.phoneSlv = phoneSlv;
    }

    private String smsPwd;// 短信校验码

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

    public String getSmsPwd() {
        return smsPwd;
    }

    public void setSmsPwd(String smsPwd) {
        this.smsPwd = smsPwd;
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
