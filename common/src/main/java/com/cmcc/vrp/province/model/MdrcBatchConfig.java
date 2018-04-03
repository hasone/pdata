package com.cmcc.vrp.province.model;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:00:16
*/
public class MdrcBatchConfig {
    private Long id;

    private String configName;

    private Long managerId;

    private Long cardmakerId;

    private Long templateId;

    private Long amount;//制卡数量

    private String provinceCode;

    private String thisYear;

    private Integer serialNumber;//批次号

    private Date createTime;

    private Long creatorId;

    private Integer status;

    private Date downloadTime;

    private String downloadIp;

    private String excelPassword;

    private Integer notiFlag;

    private Date notiTime;
    
    private Date effectiveTime;//生效日期
    
    private Date expiryTime;//失效日期
    
    //add by qinqinyan on 2017/07/28
    
    private Long enterpriseId;//企业ID
    
    private String enterpriseName;//企业名称
    
    private Long productId;
    
    private Long configInfoId; //卡批次详情id
    
    //mdrc extend info add by luozuwu on 2017/08/10 
    private Long canBeActivatedCount;//可激活数量
    
    private String productName;//产品名称
        
    private String startCardNumer;//起始卡号
    
    private String endCardNumber;//终止卡号
    
    /**
     * 扩展字段：cardmakerName用于联合查询
     */
    private String cardmakerName;

    private Long productSize;
    
    private Integer cardmakeStatus; //0:未制卡，1：已制卡；2：已准备好待下载数据
            
    private String customerServicePhone;//客服电话
    
    private String qrcodeKey;//二维码
    
    private String qrcodeName;
    
    //模板信息
    private String frontImage;//模板正面图片
    
    private String rearImage;//模板背面图片
    
    //物流信息
    private String expressEntName;//物流公司名称
    
    private String expressNumber;//快递单号
    
    //签收人信息
    private String receiverName;//签收人姓名
    
    private String receiverMobile;//签收人手机号码 
    
    private String receiveFileName;//签收凭证
    
    private String receiveKey;//签收凭证
    
    private Integer templateType;//模板类型

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

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Long getCardmakerId() {
        return cardmakerId;
    }

    public void setCardmakerId(Long cardmakerId) {
        this.cardmakerId = cardmakerId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode == null ? null : provinceCode.trim();
    }

    public String getThisYear() {
        return thisYear;
    }

    public void setThisYear(String thisYear) {
        this.thisYear = thisYear == null ? null : thisYear.trim();
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }


    public Date getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(Date downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getDownloadIp() {
        return downloadIp;
    }

    public void setDownloadIp(String downloadIp) {
        this.downloadIp = downloadIp == null ? null : downloadIp.trim();
    }

    public String getExcelPassword() {
        return excelPassword;
    }

    public void setExcelPassword(String excelPassword) {
        this.excelPassword = excelPassword == null ? null : excelPassword.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Integer getNotiFlag() {
        return notiFlag;
    }

    public void setNotiFlag(Integer notiFlag) {
        this.notiFlag = notiFlag;
    }

    public Date getNotiTime() {
        return notiTime;
    }

    public void setNotiTime(Date notiTime) {
        this.notiTime = notiTime;
    }

    public String getCardmakerName() {
        return cardmakerName;
    }

    public void setCardmakerName(String cardmakerName) {
        this.cardmakerName = cardmakerName;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
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

    public Integer getCardmakeStatus() {
        return cardmakeStatus;
    }

    public void setCardmakeStatus(Integer cardmakeStatus) {
        this.cardmakeStatus = cardmakeStatus;
    }
    public Long getCanBeActivatedCount() {
        return canBeActivatedCount;
    }

    public void setCanBeActivatedCount(Long canBeActivatedCount) {
        this.canBeActivatedCount = canBeActivatedCount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStartCardNumer() {
        return startCardNumer;
    }

    public void setStartCardNumer(String startCardNumer) {
        this.startCardNumer = startCardNumer;
    }

    public String getEndCardNumber() {
        return endCardNumber;
    }

    public void setEndCardNumber(String endCardNumber) {
        this.endCardNumber = endCardNumber;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getCustomerServicePhone() {
        return customerServicePhone;
    }

    public void setCustomerServicePhone(String customerServicePhone) {
        this.customerServicePhone = customerServicePhone;
    }

    public String getQrcodeKey() {
        return qrcodeKey;
    }

    public void setQrcodeKey(String qrcodeKey) {
        this.qrcodeKey = qrcodeKey;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }


    public String getExpressEntName() {
        return expressEntName;
    }

    public void setExpressEntName(String expressEntName) {
        this.expressEntName = expressEntName;
    }

    public String getReceiveKey() {
        return receiveKey;
    }

    public void setReceiveKey(String receiveKey) {
        this.receiveKey = receiveKey;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public String getRearImage() {
        return rearImage;
    }

    public void setRearImage(String rearImage) {
        this.rearImage = rearImage;
    }

    public String getReceiveFileName() {
        return receiveFileName;
    }

    public void setReceiveFileName(String receiveFileName) {
        this.receiveFileName = receiveFileName;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public String getQrcodeName() {
        return qrcodeName;
    }

    public void setQrcodeName(String qrcodeName) {
        this.qrcodeName = qrcodeName;
    }
    
    
}