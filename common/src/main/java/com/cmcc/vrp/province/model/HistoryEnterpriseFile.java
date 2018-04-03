package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * <p>Title:HistoryEnterpriseFile </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月18日
*/
public class HistoryEnterpriseFile {
    private Long id;

    private Long requestId;

    private Long entId;

    private String customerfileName;

    private String imageName;

    private String contractName;

    private String businessLicence;

    private String authorizationCertificate;

    private String identificationCard;

    private String identificationBack;

    private String customerfileKey;

    private String imageKey;

    private String contractKey;

    private String licenceKey;

    private String authorizationKey;

    private String identificationKey;

    private String identificationBackKey;

    private Date createTime;

    private Date updateTime;

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

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public String getCustomerfileName() {
        return customerfileName;
    }

    public void setCustomerfileName(String customerfileName) {
        this.customerfileName = customerfileName == null ? null : customerfileName.trim();
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName == null ? null : imageName.trim();
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName == null ? null : contractName.trim();
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence == null ? null : businessLicence.trim();
    }

    public String getAuthorizationCertificate() {
        return authorizationCertificate;
    }

    public void setAuthorizationCertificate(String authorizationCertificate) {
        this.authorizationCertificate = authorizationCertificate == null ? null : authorizationCertificate.trim();
    }

    public String getIdentificationCard() {
        return identificationCard;
    }

    public void setIdentificationCard(String identificationCard) {
        this.identificationCard = identificationCard == null ? null : identificationCard.trim();
    }

    public String getIdentificationBack() {
        return identificationBack;
    }

    public void setIdentificationBack(String identificationBack) {
        this.identificationBack = identificationBack == null ? null : identificationBack.trim();
    }

    public String getCustomerfileKey() {
        return customerfileKey;
    }

    public void setCustomerfileKey(String customerfileKey) {
        this.customerfileKey = customerfileKey == null ? null : customerfileKey.trim();
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey == null ? null : imageKey.trim();
    }

    public String getContractKey() {
        return contractKey;
    }

    public void setContractKey(String contractKey) {
        this.contractKey = contractKey == null ? null : contractKey.trim();
    }

    public String getLicenceKey() {
        return licenceKey;
    }

    public void setLicenceKey(String licenceKey) {
        this.licenceKey = licenceKey == null ? null : licenceKey.trim();
    }

    public String getAuthorizationKey() {
        return authorizationKey;
    }

    public void setAuthorizationKey(String authorizationKey) {
        this.authorizationKey = authorizationKey == null ? null : authorizationKey.trim();
    }

    public String getIdentificationKey() {
        return identificationKey;
    }

    public void setIdentificationKey(String identificationKey) {
        this.identificationKey = identificationKey == null ? null : identificationKey.trim();
    }

    public String getIdentificationBackKey() {
        return identificationBackKey;
    }

    public void setIdentificationBackKey(String identificationBackKey) {
        this.identificationBackKey = identificationBackKey == null ? null : identificationBackKey.trim();
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
}