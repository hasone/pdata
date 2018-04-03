package com.cmcc.vrp.province.reconcile.model;


/**
 * 对账账单模型，只包含基本数据，其它对账账单模型请拓展该类
 *
 */
public class BillModel {
    private String phone;
    
    private String enterCode;
    
    private String prdCode;
    
    private String line;
    
    //江苏拓展，记录phone+prdCode的出现次数，默认为1
    private int phonePrdCount = 1;
    
    //boss返回流水号
    private String bossRespSeq;
    
    //充值时间
    private String chargeTime;

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

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getPhonePrdCount() {
        return phonePrdCount;
    }

    public void setPhonePrdCount(int phonePrdCount) {
        this.phonePrdCount = phonePrdCount;
    }

    public String getBossRespSeq() {
        return bossRespSeq;
    }

    public void setBossRespSeq(String bossRespSeq) {
        this.bossRespSeq = bossRespSeq;
    }

    public String getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(String chargeTime) {
        this.chargeTime = chargeTime;
    }
    
    
}
