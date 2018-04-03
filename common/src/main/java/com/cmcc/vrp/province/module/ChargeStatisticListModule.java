package com.cmcc.vrp.province.module;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:44:18
*/
public class ChargeStatisticListModule {
    /**
     * 企业名称
     */
    private String eName;

    /**
     * 企业编码
     */
    private String eCode;

    /**
     * 地区
     */
    private String fullDistrictName;

    /**
     * 活动名称
     */
    private String aName;

    /**
     * 充值类型：普通赠送、包月赠送、红包等
     */
    private String chargeType;

    /**
     * 产品名称
     */
    private String pName;
    
    /**
     * 产品编码
     */
    private String pCode;

    /**
     * 充值手机号码
     */
    private String mobile;

    /**
     * 充值号码
     */
    private String chargeTime;

    /**
     * 充值状态
     */
    private Integer chargeStatus;

    /**
     * 充值信息：充值失败时的错误信息
     */
    private String ChargeMsg;

    /**
     * 具体活动充值记录表
     */
    private Long recordId;

    private String serialNum;
    
    /**
     * BOSS充值请求序列号
     */
    private String bossReqSerialNum;
    
    private String systemNum;

    /**
     * 产品原价
     */
    private Double price;
    
    private Integer isQueryChargeResult; //是否有充值结果查询接口
    
    private String fingerprint;
    //手动查询充值结果时间
    private Date queryTime;
    
    private String feature;//特征参数

    public String getEName() {
        return eName;
    }

    public void setEName(String eName) {
        this.eName = eName;
    }

    public String getECode() {
        return eCode;
    }

    public void setECode(String eCode) {
        this.eCode = eCode;
    }

    public String getFullDistrictName() {
        return fullDistrictName;
    }

    public void setFullDistrictName(String fullDistrictName) {
        this.fullDistrictName = fullDistrictName;
    }

    public String getAName() {
        return aName;
    }

    public void setAName(String aName) {
        this.aName = aName;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(String chargeTime) {
        this.chargeTime = chargeTime;
    }

    public Integer getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Integer chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getChargeMsg() {
        return ChargeMsg;
    }

    public void setChargeMsg(String chargeMsg) {
        ChargeMsg = chargeMsg;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBossReqSerialNum() {
        return bossReqSerialNum;
    }

    public void setBossReqSerialNum(String bossReqSerialNum) {
        this.bossReqSerialNum = bossReqSerialNum;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }

    public Integer getIsQueryChargeResult() {
        return isQueryChargeResult;
    }

    public void setIsQueryChargeResult(Integer isQueryChargeResult) {
        this.isQueryChargeResult = isQueryChargeResult;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Date getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

}
