package com.cmcc.vrp.wx.beans;

/**
 * 兑换充值队列中的消息所对应的对象
 * ExchangeChargePojo.java
 * @author wujiamin
 * @date 2017年3月16日
 */
public class ExchangeChargePojo {
    
    private String prdCode;
    
    private String mobile;
    
    private String systemNum;

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }
}
