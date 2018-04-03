package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * <p>Title:HistoryEnterprises </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月18日
*/
public class HistoryEnterprises {
    private Long id;

    private Long requestId;

    private Long entId;

    private String name;

    private String code;

    private String phone;

    private String email;

    private Date createTime;

    private Date updateTime;

    private Byte status;

    private Integer deleteFlag;

    private Long creatorId;

    private String appSecret;

    private String entName;

    private Long districtId;

    private Long customerTypeId;

    private Long benefitGradeId;

    private Long discount;
    
    private String discountName;//折扣信息

    private Long businessTypeId;

    private Long payTypeId;

    private Integer interfaceFlag;

    private Date startTime;

    private Date endTime;

    private String appKey;

    private Date licenceStartTime;

    private Date licenceEndTime;

    private String cmEmail;

    private String cmPhone;

    private Integer fcsmsFlag;
    
    private Long giveMoneyId;
    
    private String giveMoneyName;

    private String comment; //修改备注

    //extended

    private String cmManagerName; //企业管理员的父节点
    
    /**
     * @return the giveMoneyName
     */
    public String getGiveMoneyName() {
        return giveMoneyName;
    }

    /**
     * @param giveMoneyName the giveMoneyName to set
     */
    public void setGiveMoneyName(String giveMoneyName) {
        this.giveMoneyName = giveMoneyName;
    }

    /**
     * @return the discountName
     */
    public String getDiscountName() {
        return discountName;
    }

    /**
     * @param discountName the discountName to set
     */
    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    /**
     * @return the giveMoneyId
     */
    public Long getGiveMoneyId() {
        return giveMoneyId;
    }

    /**
     * @param giveMoneyId the giveMoneyId to set
     */
    public void setGiveMoneyId(Long giveMoneyId) {
        this.giveMoneyId = giveMoneyId;
    }

    /**
     * @return the cmManagerName
     */
    public String getCmManagerName() {
        return cmManagerName;
    }

    /**
     * @param cmManagerName the cmManagerName to set
     */
    public void setCmManagerName(String cmManagerName) {
        this.cmManagerName = cmManagerName;
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

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret == null ? null : appSecret.trim();
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName == null ? null : entName.trim();
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(Long customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    public Long getBenefitGradeId() {
        return benefitGradeId;
    }

    public void setBenefitGradeId(Long benefitGradeId) {
        this.benefitGradeId = benefitGradeId;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public Long getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Long businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public Long getPayTypeId() {
        return payTypeId;
    }

    public void setPayTypeId(Long payTypeId) {
        this.payTypeId = payTypeId;
    }

    public Integer getInterfaceFlag() {
        return interfaceFlag;
    }

    public void setInterfaceFlag(Integer interfaceFlag) {
        this.interfaceFlag = interfaceFlag;
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

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey == null ? null : appKey.trim();
    }

    public Date getLicenceStartTime() {
        return licenceStartTime;
    }

    public void setLicenceStartTime(Date licenceStartTime) {
        this.licenceStartTime = licenceStartTime;
    }

    public Date getLicenceEndTime() {
        return licenceEndTime;
    }

    public void setLicenceEndTime(Date licenceEndTime) {
        this.licenceEndTime = licenceEndTime;
    }

    public String getCmEmail() {
        return cmEmail;
    }

    public void setCmEmail(String cmEmail) {
        this.cmEmail = cmEmail == null ? null : cmEmail.trim();
    }

    public String getCmPhone() {
        return cmPhone;
    }

    public void setCmPhone(String cmPhone) {
        this.cmPhone = cmPhone == null ? null : cmPhone.trim();
    }

    public Integer getFcsmsFlag() {
        return fcsmsFlag;
    }

    public void setFcsmsFlag(Integer fcsmsFlag) {
        this.fcsmsFlag = fcsmsFlag;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}