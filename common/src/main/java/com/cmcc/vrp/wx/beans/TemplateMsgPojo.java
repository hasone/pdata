package com.cmcc.vrp.wx.beans;

import com.cmcc.vrp.enums.WxTemplateMsgType;

/**
 * 模板消息发送队列中所对应的对象
 * TemplateMsgPojo.java
 * @author wujiamin
 * @date 2017年3月29日
 */
public class TemplateMsgPojo {
    private WxTemplateMsgType type;
    private String mobile;
    private String activityId;
    private String activityWinRecordId;
    private String paymentSerial;
    private String exchangeSystemNum;
    public String getActivityId() {
        return activityId;
    }
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
    public String getActivityWinRecordId() {
        return activityWinRecordId;
    }
    public void setActivityWinRecordId(String activityWinRecordId) {
        this.activityWinRecordId = activityWinRecordId;
    }
    public String getPaymentSerial() {
        return paymentSerial;
    }
    public void setPaymentSerial(String paymentSerial) {
        this.paymentSerial = paymentSerial;
    }
    public String getExchangeSystemNum() {
        return exchangeSystemNum;
    }
    public void setExchangeSystemNum(String exchangeSystemNum) {
        this.exchangeSystemNum = exchangeSystemNum;
    }
    public WxTemplateMsgType getType() {
        return type;
    }
    public void setType(WxTemplateMsgType type) {
        this.type = type;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
