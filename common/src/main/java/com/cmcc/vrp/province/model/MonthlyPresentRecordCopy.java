package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * @author lgk8023
 *
 */
public class MonthlyPresentRecordCopy {
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
}