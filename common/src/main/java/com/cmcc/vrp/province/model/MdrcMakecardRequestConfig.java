package com.cmcc.vrp.province.model;

import java.util.Date;
/**
 * 制卡操作：审核请求于制卡关系类
 * */
public class MdrcMakecardRequestConfig {
    private Long id;

    private Long configId;

    private Long requestId;

    private Date createTime;

    private Integer deleteFlag;
    
    //extended
    private String fileName; //待下载文件的文件名
    
    private Integer cardmakeStatus; //0:未制卡，1：已制卡；2：已准备好待下载数据
    
    private Long mdrcCardmakeDetailId;
    
    private Long cardmakerId;
    
    private Long templateId;
    
    private Long amount;

    private Date startTime;

    private Date endTime;
    
    private Long enterpriseId;
    
    private Long productId;
    
    private Long configInfoId; //卡批次详情id
    
    private String trackingNumber; //物流单号
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getCardmakeStatus() {
        return cardmakeStatus;
    }

    public void setCardmakeStatus(Integer cardmakeStatus) {
        this.cardmakeStatus = cardmakeStatus;
    }

    public Long getMdrcCardmakeDetailId() {
        return mdrcCardmakeDetailId;
    }

    public void setMdrcCardmakeDetailId(Long mdrcCardmakeDetailId) {
        this.mdrcCardmakeDetailId = mdrcCardmakeDetailId;
    }

    public Long getCardmakerId() {
        return cardmakerId;
    }

    public void setCardmakerId(Long cardmakerId) {
        this.cardmakerId = cardmakerId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getConfigInfoId() {
        return configInfoId;
    }

    public void setConfigInfoId(Long configInfoId) {
        this.configInfoId = configInfoId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
    
}