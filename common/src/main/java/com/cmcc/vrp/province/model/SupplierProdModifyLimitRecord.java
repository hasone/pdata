package com.cmcc.vrp.province.model;

import java.util.Date;
/**
 * 供应商产品变更限额记录对象
 * @author qinqinyan
 * */
public class SupplierProdModifyLimitRecord {
    private Long id;

    private Long supplierProductId;

    private Double limitMoney;

    private Integer operateType;

    private Long operateId;

    private Date createTime;

    private Integer deleteFlag;

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

    public Double getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(Double limitMoney) {
        this.limitMoney = limitMoney;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public Long getOperateId() {
        return operateId;
    }

    public void setOperateId(Long operateId) {
        this.operateId = operateId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}