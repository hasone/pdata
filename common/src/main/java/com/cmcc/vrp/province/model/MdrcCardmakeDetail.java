package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 制卡审核详情类
 * */
public class MdrcCardmakeDetail {
    private Long id;

    private Long requestId;

    private String configName;

    private Long cardmakerId;

    private Long templateId;

    private Long amount;

    private Date startTime;

    private Date endTime;

    private Long deleteFlag;

    private Integer cardmakeStatus; //0:未制卡；1已制卡；2：已经准备好数据

    //add by qinqinyan on 2017/07/28

    private Long enterpriseId;

    private Long productId;

    private Long configInfoId; //卡批次详情id

    private String fileName; //下载文件的文件名

    private String trackingNumber; //快递单号
    
    private Long cost;//制卡成本

    public Integer getCardmakeStatus() {
        return cardmakeStatus;
    }

    public void setCardmakeStatus(Integer cardmakeStatus) {
        this.cardmakeStatus = cardmakeStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName == null ? null : configName.trim();
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

    public Long getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Long deleteFlag) {
        this.deleteFlag = deleteFlag;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }


    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}