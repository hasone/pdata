package com.cmcc.vrp.province.model;

import java.util.Date;
/**
 * 供应商财务记录对象
 * @author qinqinyan
 * */
public class SupplierFinanceRecord {
    private Long id;

    private Long supplierId;

    private Double totalMoney;

    private Double usedMoney;

    private Double balance;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;
    
    private Date operateTime; //记录操作增加付款记录或者废弃付款记录的操作时间，这个是产品要求的
    
    //extends
    private String supplierName;
    
    private Double totalUseMoney;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Double getUsedMoney() {
        return usedMoney;
    }

    public void setUsedMoney(Double usedMoney) {
        this.usedMoney = usedMoney;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Double getTotalUseMoney() {
        return totalUseMoney;
    }

    public void setTotalUseMoney(Double totalUseMoney) {
        this.totalUseMoney = totalUseMoney;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
    
    
}