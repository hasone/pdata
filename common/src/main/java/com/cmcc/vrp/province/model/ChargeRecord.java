package com.cmcc.vrp.province.model;

import java.util.Date;


/**
    * 充值记录类
    *
    * */
public class ChargeRecord {
    private Long id;

    private Long prdId;

    private Long enterId;

    private Integer typeCode;

    private Long recordId;

    private Date chargeTime;

    private Integer status;

    private String statusCode;

    private String errorMessage;

    private String type;

    private String phone;

    private String aName;//活动名称

    private String systemNum;

    private String serialNum;

    private String bossNum;

    private String fingerprint;

    private Long price;//该笔充值的价格

    private Long supplierProductId;
    
    private Date updateChargeTime;
    
    private Integer financeStatus;
    
    private Integer changeAccountStatus;//0-已出账，1-调账中
    
    private Date bossChargeTime;

    //extended
    private String entName; //企业名称

    private String productName; //产品名称

    private String district; //归属地

    private String timeRange; //请求时长
    
    private Integer effectType;
    
    private Long supplierProductPrice;
    
    private Integer count;//充值流量包个数，db默认为1
    
    //MDRC充值记录
    private String configSerialNumber;//批次号
    
    private String cardNumber;//卡号
    
    private String configName;//卡名称
    
    //手动查询充值结果时间
    private Date queryTime;

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }


    /**
     *  @return the aName
     * */
    public String getaName() {
        return aName;
    }

    /**
     * @param aName the aName to set
     * */
    public void setaName(String aName) {
        this.aName = aName;
    }


    /**
     * @return the statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getEnterId() {
        return enterId;
    }

    public void setEnterId(Long enterId) {
        this.enterId = enterId;
    }

    public Integer getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Date getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Date chargeTime) {
        this.chargeTime = chargeTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage == null ? null : errorMessage.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum == null ? null : systemNum.trim();
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum == null ? null : serialNum.trim();
    }

    public String getBossNum() {
        return bossNum;
    }

    public void setBossNum(String bossNum) {
        this.bossNum = bossNum == null ? null : bossNum.trim();
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getSupplierProductId() {
        return supplierProductId;
    }

    public void setSupplierProductId(Long supplierProductId) {
        this.supplierProductId = supplierProductId;
    }

    public Date getUpdateChargeTime() {
        return updateChargeTime;
    }

    public void setUpdateChargeTime(Date updateChargeTime) {
        this.updateChargeTime = updateChargeTime;
    }

    public Integer getFinanceStatus() {
        return financeStatus;
    }

    public void setFinanceStatus(Integer financeStatus) {
        this.financeStatus = financeStatus;
    }

    public Integer getChangeAccountStatus() {
        return changeAccountStatus;
    }

    public void setChangeAccountStatus(Integer changeAccountStatus) {
        this.changeAccountStatus = changeAccountStatus;
    }

    public Date getBossChargeTime() {
        return bossChargeTime;
    }

    public void setBossChargeTime(Date bossChargeTime) {
        this.bossChargeTime = bossChargeTime;
    }

    public Integer getEffectType() {
        return effectType;
    }

    public void setEffectType(Integer effectType) {
        this.effectType = effectType;
    }

    public Long getSupplierProductPrice() {
        return supplierProductPrice;
    }

    public void setSupplierProductPrice(Long supplierProductPrice) {
        this.supplierProductPrice = supplierProductPrice;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getConfigSerialNumber() {
        return configSerialNumber;
    }

    public void setConfigSerialNumber(String configSerialNumber) {
        this.configSerialNumber = configSerialNumber;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Date getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }
    
    

}