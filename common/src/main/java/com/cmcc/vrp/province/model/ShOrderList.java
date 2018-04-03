package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * @author lgk8023
 *
 */

public class ShOrderList {

    private Long id;
   
    private Long enterId;
   
    private String mainBillId;
   
    private String orderName;
   
    private String offerId;
   
    private String roleId;
   
    private String orderType;
    
    private String accId;
    
    private Double count;
    
    private Double alertCount;
    
    private Double stopCount;
    
    private Date createTime;

    private Date updateTime;
    
    private Integer deleteFlag;

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

    public String getMainBillId() {
        return mainBillId;
    }

    public void setMainBillId(String mainBillId) {
        this.mainBillId = mainBillId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
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

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    
}
