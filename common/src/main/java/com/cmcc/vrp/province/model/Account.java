package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 账户对象
 */
public class Account {
    private Long id;

    private Long enterId;

    private Long ownerId;

    private Long productId;

    private Double count;

    private Integer type;

    private Double minCount;

    /**
     * 告警值
     */
    private Double alertCount;

    /**
     * 暂停值
     */
    private Double stopCount;

    private Date createTime;

    private Date updateTime;

    private Byte deleteFlag;

    private Integer version;

    // 附加字段
    private Long productSize;

    private Integer productType;

    private String productName;

    private String productTypeName;

    public Long getProductSize() {
        return productSize;
    }

    public void setProductSize(Long productSize) {
        this.productSize = productSize;
    }

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Double getMinCount() {
        return minCount;
    }

    public void setMinCount(Double minCount) {
        this.minCount = minCount;
    }

    public Double getAlertCount() {
        return alertCount;
    }

    public void setAlertCount(Double alertCount) {
        this.alertCount = alertCount;
    }

    public Double getStopCount() {
        return stopCount;
    }

    public void setStopCount(Double stopCount) {
        this.stopCount = stopCount;
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

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }
}
