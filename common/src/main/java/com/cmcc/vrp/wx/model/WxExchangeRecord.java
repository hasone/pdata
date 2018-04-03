package com.cmcc.vrp.wx.model;

import java.util.Date;

public class WxExchangeRecord {
    private Long id;

    private Long adminId;

    private String mobile;

    private Long individualProductId;
    
    private Integer count;//兑换需要的流量币个数

    private String systemNum;

    private String bossReqSerialNum;

    private String bossRespSerialNum;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;
    
    private String message;
    
    //extends
    private String ownerMobile;
    
    private String productName;
    
    private Integer productSize;    

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Long getIndividualProductId() {
        return individualProductId;
    }

    public void setIndividualProductId(Long individualProductId) {
        this.individualProductId = individualProductId;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum == null ? null : systemNum.trim();
    }

    public String getBossReqSerialNum() {
        return bossReqSerialNum;
    }

    public void setBossReqSerialNum(String bossReqSerialNum) {
        this.bossReqSerialNum = bossReqSerialNum == null ? null : bossReqSerialNum.trim();
    }

    public String getBossRespSerialNum() {
        return bossRespSerialNum;
    }

    public void setBossRespSerialNum(String bossRespSerialNum) {
        this.bossRespSerialNum = bossRespSerialNum == null ? null : bossRespSerialNum.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getOwnerMobile() {
        return ownerMobile;
    }

    public void setOwnerMobile(String ownerMobile) {
        this.ownerMobile = ownerMobile;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductSize() {
        return productSize;
    }

    public void setProductSize(Integer productSize) {
        this.productSize = productSize;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}