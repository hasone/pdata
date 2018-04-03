package com.cmcc.vrp.province.model;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:08:49
*/
public class ShBossProduct {
    private Long id;

    private Long supplierProductId;

    private Integer supplierProductPrice;

    private Long supplierProductSize;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;
    
    private String supplierProductName;
    
    private String orderType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierProductId() {
        return supplierProductId;
    }

    public void setSupplierProductId(Long supplierProductId) {
        this.supplierProductId = supplierProductId;
    }

    public Integer getSupplierProductPrice() {
        return supplierProductPrice;
    }

    public void setSupplierProductPrice(Integer supplierProductPrice) {
        this.supplierProductPrice = supplierProductPrice;
    }

    public Long getSupplierProductSize() {
        return supplierProductSize;
    }

    public void setSupplierProductSize(Long supplierProductSize) {
        this.supplierProductSize = supplierProductSize;
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

    public String getSupplierProductName() {
        return supplierProductName;
    }

    public void setSupplierProductName(String supplierProductName) {
        this.supplierProductName = supplierProductName;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}