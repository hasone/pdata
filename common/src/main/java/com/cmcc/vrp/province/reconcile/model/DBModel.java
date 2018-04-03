package com.cmcc.vrp.province.reconcile.model;

import java.util.Date;

/**
 * 对账数据库模型，只包含基本数据，其它对账账单模型请拓展该类
 *
 */
public class DBModel {
    private String phone;
    
    private String enterCode;
    
    private String prdCode;
    
    //江苏拓展，记录phone+prdCode的出现次数，默认为1
    private int phonePrdCount = 1;
    
    //boss返回流水号
    private String bossRespSerialNum;
    
    //充值时间
    private Date chargeTime;
    

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEnterCode() {
        return enterCode;
    }

    public void setEnterCode(String enterCode) {
        this.enterCode = enterCode;
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public int getPhonePrdCount() {
        return phonePrdCount;
    }

    public void setPhonePrdCount(int phonePrdCount) {
        this.phonePrdCount = phonePrdCount;
    }

    public String getBossRespSerialNum() {
        return bossRespSerialNum;
    }

    public void setBossRespSerialNum(String bossRespSerialNum) {
        this.bossRespSerialNum = bossRespSerialNum;
    }

    public Date getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Date chargeTime) {
        this.chargeTime = chargeTime;
    }
    
    
}
