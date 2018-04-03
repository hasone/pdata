package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 
 * @ClassName: MonthlyPresentRecord 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年7月18日 下午4:15:45
 */
public class MonthlyPresentRecord {

    private Long id;

    private Long ruleId;

    private Long prdId;

    private String mobile;

    private Byte status;

    private Integer effectType;

    private String statusCode;

    private String errorMessage;

    private Date createTime;

    private Date operateTime;

    private String sysSerialNum;

    private String bossSerialNum;
    
    private Integer giveMonth;

    //以下为扩展属性
    private Long entId;

    private String entName;

    private String entCode;

    private String productName;

    private String productCode;
    
    private Long productSize;

    private Integer monthCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getEffectType() {
        return effectType;
    }

    public void setEffectType(Integer effectType) {
        this.effectType = effectType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode == null ? null : statusCode.trim();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage == null ? null : errorMessage.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getSysSerialNum() {
        return sysSerialNum;
    }

    public void setSysSerialNum(String sysSerialNum) {
        this.sysSerialNum = sysSerialNum == null ? null : sysSerialNum.trim();
    }

    public String getBossSerialNum() {
        return bossSerialNum;
    }

    public void setBossSerialNum(String bossSerialNum) {
        this.bossSerialNum = bossSerialNum == null ? null : bossSerialNum.trim();
    }

    public Integer getGiveMonth() {
        return giveMonth;
    }

    public void setGiveMonth(Integer giveMonth) {
        this.giveMonth = giveMonth;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getEntCode() {
        return entCode;
    }

    public void setEntCode(String entCode) {
        this.entCode = entCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getProductSize() {
        return productSize;
    }

    public void setProductSize(Long productSize) {
        this.productSize = productSize;
    }

    public Integer getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(Integer monthCount) {
        this.monthCount = monthCount;
    }
}