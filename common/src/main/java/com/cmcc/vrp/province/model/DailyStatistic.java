package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * mysql-generator
 *
 */
public class DailyStatistic {
    private Long id;

    private Long enterId;

    private String city;
    
    private Long prdId;

    private String chargeType;

    private Long successCount;

    private Long totalCount;

    private Long capacity;

    private Long money;

    private Date date;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;
    
    //以下为扩展属性
    
    private String productName;//产品名称
    
    private String productCode;//产品编码
    
    private Integer productType;//产品类型
    
    private Long productPrice;//产品价格

    private Long productSize;//产品大小

    private Long totalSuccessCapacity;//充值成功总大小

    private Long totalSuccessCount;//充值成功总个数

    private Long totalSuccessMoney;//充值成功总额度
    
    private Double successRate; //充值成功率
    
    private String enterCode;//企业编码
    
    private String enterName;//企业名称

    public Long getId() {
        return id; 
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnterId() {
        return enterId;
    }

    public void setEnterId(Long enterId) {
        this.enterId = enterId;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public Long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Long productPrice) {
        this.productPrice = productPrice;
    }

    public Long getProductSize() {
        return productSize;
    }

    public void setProductSize(Long productSize) {
        this.productSize = productSize;
    }

    public Long getTotalSuccessCapacity() {
        return totalSuccessCapacity;
    }

    public void setTotalSuccessCapacity(Long totalSuccessCapacity) {
        this.totalSuccessCapacity = totalSuccessCapacity;
    }

    public Long getTotalSuccessCount() {
        return totalSuccessCount;
    }

    public void setTotalSuccessCount(Long totalSuccessCount) {
        this.totalSuccessCount = totalSuccessCount;
    }

    public Long getTotalSuccessMoney() {
        return totalSuccessMoney;
    }

    public void setTotalSuccessMoney(Long totalSuccessMoney) {
        this.totalSuccessMoney = totalSuccessMoney;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }


    public String getEnterCode() {
        return enterCode;
    }

    public void setEnterCode(String enterCode) {
        this.enterCode = enterCode;
    }

    public String getEnterName() {
        return enterName;
    }

    public void setEnterName(String enterName) {
        this.enterName = enterName;
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

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }
}