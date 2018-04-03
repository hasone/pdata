package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * IndividualFlowOrder
 */
public class IndividualFlowOrder {
    private Long id;

    private String mobile;

    private Long prdId;

    private Long size;

    private String systemNum;

    private String ecSerialNum;

    private String bossSerialNum;

    private Integer status;

    private String errorMsg;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;
    
    private Date expireTime;
    
    //extend
    private Long productSize;

    private Integer canCreateRedpacket;//是否可以生成红包，0-不可以，1-可以
    
    private Long price;
    
    private String productName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum == null ? null : systemNum.trim();
    }

    public String getEcSerialNum() {
        return ecSerialNum;
    }

    public void setEcSerialNum(String ecSerialNum) {
        this.ecSerialNum = ecSerialNum == null ? null : ecSerialNum.trim();
    }

    public String getBossSerialNum() {
        return bossSerialNum;
    }

    public void setBossSerialNum(String bossSerialNum) {
        this.bossSerialNum = bossSerialNum == null ? null : bossSerialNum.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
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

    public Long getProductSize() {
        return productSize;
    }

    public void setProductSize(Long productSize) {
        this.productSize = productSize;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}