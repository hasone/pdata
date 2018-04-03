package com.cmcc.vrp.boss.shangdong.boss.model;

import java.util.Date;

/**
 * 山东发短信的基本对象，从云平台的类移植
 *
 */
public class SmsIctParam {

    /**
     * 手机号
     */
    private String mobs;
    
    /**
     * 内容
     */
    private String content;
    
    /**
     * 序列号
     */
    private String serialNumber;
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;

    public String getMobs() {
        return mobs;
    }


    public void setMobs(String mobs) {
        this.mobs = mobs;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public String getSerialNumber() {
        return serialNumber;
    }


    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public Date getStartTime() {
        return startTime;
    }


    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public Date getEndTime() {
        return endTime;
    }


    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    
}
