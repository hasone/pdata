package com.cmcc.vrp.province.model;

import java.util.Date;

import com.cmcc.vrp.exception.SelfCheckException;
import com.cmcc.vrp.util.PropertyValidator;

/**
 * 
 * @ClassName: Enterprise 
 * @Description: TODO
 */
public class Enterprise {
    private Long id;

    private String name;

    private String code;

    private String phone;

    private String appSecret;

    private Long creatorId;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    private String entName; //企业品牌名

    private Long districtId; //企业地区Id

    private Long customerTypeId;//客户分类Id

    private Long businessTypeId;//业务类型Id

    private Long payTypeId; //支付类型Id

    private String customerTypeName;

    private String businessTypeName;

    private String payTypeName;

    private Long discount; //折扣

    private String discountName;//折扣信息

    private Date startTime; //合作开始时间

    private Date endTime; //合作结束时间

    private Integer interfaceFlag; //接口调用标识

    private Long managerId;//所管理的客户经理Id

    private String appKey;

    private Double currencyCount;

    private Double minCount;
    
    /**
     * 告警值
     */
    private Double alertCount;
    
    /**
     * 暂停值
     */
    private Double stopCount;
    private Long giveMoneyId;//存送比的id

    private String giveMoneyName;//存送比的描述

    private String email;//企业邮箱

    private Integer productChangeStatus;//企业产品变更状态

    private Date licenceStartTime;//营业执照开始时间

    private Date licenceEndTime;//营业执照结束时间

    private Byte status;

    private String cmPhone;//客户经理手机号码

    private String cmEmail;//客户经理邮箱
    
    private Date interfaceExpireTime;//接口appkey,appsecret过期时间
    
    private Integer interfaceApprovalStatus; //接口信息变更审核状态

    //extended

    private String customerManagerPhone;

    private String customerManagerName;

    private String enterpriseManagerName;

    private String enterpriseManagerPhone;

    private String cmManagerName; //企业管理员的父节点

    private String description; //用于描述企业当前审核状态
    
    private String enterpriseCity;//企业所属的地市信息
    
    private Long productId;//产品ID
    
    private Integer productType;//产品类型
    
    private String productName;//产品名称
    
    //extend for product template
    
    private Date mapCreateTime;//企业与产品关联时间
    
    private Long productTemplateId;//产品模板id
    
    private Long mapId; //企业与产品模板关联关系id

    private String productTemplateName; //产品模板

    private String productTypeName;//产品类型名称

    private String ecPrdCode; //广东集团产品编码

    private String scUserId;//四川202编码
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerManagerName() {
        return customerManagerName;
    }

    public void setCustomerManagerName(String customerManagerName) {
        this.customerManagerName = customerManagerName;
    }


    public String getEnterpriseManagerPhone() {
        return enterpriseManagerPhone;
    }

    public void setEnterpriseManagerPhone(String enterpriseManagerPhone) {
        this.enterpriseManagerPhone = enterpriseManagerPhone;
    }

    public String getEnterpriseManagerName() {
        return enterpriseManagerName;
    }

    public void setEnterpriseManagerName(String enterpriseManagerName) {
        this.enterpriseManagerName = enterpriseManagerName;
    }

    public String getCustomerManagerPhone() {
        return customerManagerPhone;
    }

    public void setCustomerManagerPhone(String customerManagerPhone) {
        this.customerManagerPhone = customerManagerPhone;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret == null ? null : appSecret;
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

    /**
     * 
     * @Title: selfCheck 
     * @Description: 参数检查
     * @return
     * @throws SelfCheckException
     * @return: boolean
     */
    public boolean selfCheck() throws SelfCheckException {
        //检查name没有特殊字符且长度不大于64
        PropertyValidator.isChineseLowerNumberUnderline(name, "企业名称");
        PropertyValidator.maxLengthCheck(name, 64, "企业名称");

        //检查name没有特殊字符且长度不大于64
        PropertyValidator.isChineseLowerNumberUnderline(entName, "企业简称");
        PropertyValidator.maxLengthCheck(entName, 64, "企业简称");

        //检验通过返回true
        return true;

    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
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

    public Long getPayTypeId() {
        return payTypeId;
    }

    public void setPayTypeId(Long payTypeId) {
        this.payTypeId = payTypeId;
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

    public Integer getInterfaceFlag() {
        return interfaceFlag;
    }

    public void setInterfaceFlag(Integer interfaceFlag) {
        this.interfaceFlag = interfaceFlag;
    }

    public Long getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Long businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Double getCurrencyCount() {
        return currencyCount;
    }

    public void setCurrencyCount(Double currencyCount) {
        this.currencyCount = currencyCount;
    }

    public Double getMinCount() {
        return minCount;
    }

    public void setMinCount(Double minCount) {
        this.minCount = minCount;
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
    
    public Long getGiveMoneyId() {
        return giveMoneyId;
    }

    public void setGiveMoneyId(Long giveMoneyId) {
        this.giveMoneyId = giveMoneyId;
    }

    public String getGiveMoneyName() {
        return giveMoneyName;
    }

    public void setGiveMoneyName(String giveMoneyName) {
        this.giveMoneyName = giveMoneyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getProductChangeStatus() {
        return productChangeStatus;
    }

    public void setProductChangeStatus(Integer productChangeStatus) {
        this.productChangeStatus = productChangeStatus;
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

    public String getCmManagerName() {
        return cmManagerName;
    }

    public void setCmManagerName(String cmManagerName) {
        this.cmManagerName = cmManagerName;
    }

    public String getCmPhone() {
        return cmPhone;
    }

    public void setCmPhone(String cmPhone) {
        this.cmPhone = cmPhone;
    }

    public String getCmEmail() {
        return cmEmail;
    }

    public void setCmEmail(String cmEmail) {
        this.cmEmail = cmEmail;
    }

    public Date getInterfaceExpireTime() {
        return interfaceExpireTime;
    }

    public void setInterfaceExpireTime(Date interfaceExpireTime) {
        this.interfaceExpireTime = interfaceExpireTime;
    }

    public Integer getInterfaceApprovalStatus() {
        return interfaceApprovalStatus;
    }

    public void setInterfaceApprovalStatus(Integer interfaceApprovalStatus) {
        this.interfaceApprovalStatus = interfaceApprovalStatus;
    }

    public String getEnterpriseCity() {
	return enterpriseCity;
    }

    public void setEnterpriseCity(String enterpriseCity) {
	this.enterpriseCity = enterpriseCity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }
    
    public Date getMapCreateTime() {
        return mapCreateTime;
    }

    public void setMapCreateTime(Date mapCreateTime) {
        this.mapCreateTime = mapCreateTime;
    }

    public Long getProductTemplateId() {
        return productTemplateId;
    }

    public void setProductTemplateId(Long productTemplateId) {
        this.productTemplateId = productTemplateId;
    }

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public String getProductTemplateName() {
        return productTemplateName;
    }

    public void setProductTemplateName(String productTemplateName) {
        this.productTemplateName = productTemplateName;
    }

    public String getEcPrdCode() {
        return ecPrdCode;
    }

    public void setEcPrdCode(String ecPrdCode) {
        this.ecPrdCode = ecPrdCode;
    }
    
    public String getScUserId() {
        return scUserId;
    }

    public void setScUserId(String scUserId) {
        this.scUserId = scUserId;
    }
}
