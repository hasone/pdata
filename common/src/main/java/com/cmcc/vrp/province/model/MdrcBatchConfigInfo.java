package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * create by qinqinyan 
 * */
public class MdrcBatchConfigInfo {
    private Long id;

    private Integer isFree;

    private String name;

    private String mobile;

    private String address;

    private String postcode;

    private String certificateKey;

    private String certificateName;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;
    
    private String qrcodeKey;
    
    private String qrcodeName;
    
    private String customerServicePhone;
    
    private String templateFrontKey; //自定义模板正面
    
    private String templateFrontName;
    
    private String templateBackKey;
    
    private String templateBackName;
    
    private String expressEntName;//快递公司

    private String expressNumber;//快递单号

    private String receiveKey;//签收凭证 
    
    private String receiveFileName;//签收凭证文件名
    
    private String receiverName;//签收人姓名
    
    private String receiverMobile;//签收人手机号码
     
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode == null ? null : postcode.trim();
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

    public String getQrcodeKey() {
        return qrcodeKey;
    }

    public void setQrcodeKey(String qrcodeKey) {
        this.qrcodeKey = qrcodeKey;
    }

    public String getQrcodeName() {
        return qrcodeName;
    }

    public void setQrcodeName(String qrcodeName) {
        this.qrcodeName = qrcodeName;
    }

    public String getCustomerServicePhone() {
        return customerServicePhone;
    }

    public void setCustomerServicePhone(String customerServicePhone) {
        this.customerServicePhone = customerServicePhone;
    }

    public String getCertificateKey() {
        return certificateKey;
    }

    public void setCertificateKey(String certificateKey) {
        this.certificateKey = certificateKey;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getTemplateFrontKey() {
        return templateFrontKey;
    }

    public void setTemplateFrontKey(String templateFrontKey) {
        this.templateFrontKey = templateFrontKey;
    }

    public String getTemplateFrontName() {
        return templateFrontName;
    }

    public void setTemplateFrontName(String templateFrontName) {
        this.templateFrontName = templateFrontName;
    }

    public String getTemplateBackKey() {
        return templateBackKey;
    }

    public void setTemplateBackKey(String templateBackKey) {
        this.templateBackKey = templateBackKey;
    }

    public String getTemplateBackName() {
        return templateBackName;
    }

    public void setTemplateBackName(String templateBackName) {
        this.templateBackName = templateBackName;
    }

    public String getExpressEntName() {
        return expressEntName;
    }

    public void setExpressEntName(String expressEntName) {
        this.expressEntName = expressEntName;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public String getReceiveKey() {
        return receiveKey;
    }

    public void setReceiveKey(String receiveKey) {
        this.receiveKey = receiveKey;
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

    public String getReceiveFileName() {
        return receiveFileName;
    }

    public void setReceiveFileName(String receiveFileName) {
        this.receiveFileName = receiveFileName;
    }
    
    
    
}