package com.cmcc.vrp.province.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 集中平台个人账户
 *
 * */
public class IndividualAccount {
    private Long id;

    private Long adminId;

    private Long ownerId;

    private Long individualProductId;

    private Integer type;

    private BigDecimal count;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    private Integer version;
    
    private Date expireTime;
    
    private Long currentOrderId;
    
    private Integer canCreateRedpacket;//0-不能生成红包；1-可以生成红包

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getIndividualProductId() {
        return individualProductId;
    }

    public void setIndividualProductId(Long individualProductId) {
        this.individualProductId = individualProductId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getCanCreateRedpacket() {
        return canCreateRedpacket;
    }

    public void setCanCreateRedpacket(Integer canCreateRedpacket) {
        this.canCreateRedpacket = canCreateRedpacket;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Long getCurrentOrderId() {
        return currentOrderId;
    }

    public void setCurrentOrderId(Long currentOrderId) {
        this.currentOrderId = currentOrderId;
    }

}