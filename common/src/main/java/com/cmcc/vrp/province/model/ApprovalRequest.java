package com.cmcc.vrp.province.model;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:44:06
*/
public class ApprovalRequest {
    private Long id;

    private Long processId;

    private Long entId;

    private Long creatorId;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    private Integer result;

    //extended for ent
    private String entName;

    private String entCode;

    private String entPhone;

    private Date entCreateTime;

    private String entDistrictName;//企业所属地区信息

    //extended for account
    private Double count;

    private String customerManagerName; //客户经理

    private String customerManagerMobile; //客户经理

    private String description; //状态描述

    private String districtName;
    
    private Integer discountType;
    
    private Integer discountValue;

    //extended for activity
    
    private String activityId;

    private String activityName;

    private Integer activityType;

    private String managerName; //企业管理员姓名

    private String managerPhone;//企业管理员手机号

    //extended for ent change

    private Integer entStatus;

    //extended for mdrc active

    private String startCardNumber; //起始序列号

    private String endCardNumber; //终止序列号

    private Long mdrcCount; //激活数量

    private String templateName; //营销模板名称

    private String productName; //产品名称

    private Integer activeStatus; //是否激活标识
    
    private String serialNumber;//批次号
    
    //extended for mdrc cardmake
    private String configName; //卡名称

    private Long amount; //生成卡记录

    private Integer cardmakeStatus;//处理状态（是否完成制卡）

    private String cardmakerName;//制卡商

    private Integer productType; //产品类型

    private String productTypeName;//产品类型名称
    
    private Long productSize; //产品大小

    //审批类型
    private Integer type;
    
    private String canOperate; //是否能进行审批操作，0不可以，1可以，目前该字段用于营销卡制卡审批列表
    
     
    public Integer getCardmakeStatus() {
        return cardmakeStatus;
    }

    public void setCardmakeStatus(Integer cardmakeStatus) {
        this.cardmakeStatus = cardmakeStatus;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCardmakerName() {
        return cardmakerName;
    }

    public void setCardmakerName(String cardmakerName) {
        this.cardmakerName = cardmakerName;
    }

    public Long getMdrcCount() {
        return mdrcCount;
    }

    public void setMdrcCount(Long mdrcCount) {
        this.mdrcCount = mdrcCount;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStartCardNumber() {
        return startCardNumber;
    }

    public void setStartCardNumber(String startCardNumber) {
        this.startCardNumber = startCardNumber;
    }

    public String getEndCardNumber() {
        return endCardNumber;
    }

    public void setEndCardNumber(String endCardNumber) {
        this.endCardNumber = endCardNumber;
    }

    /**
     * @return the result
     */
    public Integer getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getEntStatus() {
        return entStatus;
    }

    public void setEntStatus(Integer entStatus) {
        this.entStatus = entStatus;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getEntCode() {
        return entCode;
    }

    public void setEntCode(String entCode) {
        this.entCode = entCode;
    }

    public Date getEntCreateTime() {
        return entCreateTime;
    }

    public void setEntCreateTime(Date entCreateTime) {
        this.entCreateTime = entCreateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public Integer getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Integer discountValue) {
        this.discountValue = discountValue;
    }

    public String getCustomerManagerName() {
        return customerManagerName;
    }

    public void setCustomerManagerName(String customerManagerName) {
        this.customerManagerName = customerManagerName;
    }

    public String getCustomerManagerMobile() {
        return customerManagerMobile;
    }

    public void setCustomerManagerMobile(String customerManagerMobile) {
        this.customerManagerMobile = customerManagerMobile;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getEntPhone() {
        return entPhone;
    }

    public void setEntPhone(String entPhone) {
        this.entPhone = entPhone;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getEntDistrictName() {
        return entDistrictName;
    }

    public void setEntDistrictName(String entDistrictName) {
        this.entDistrictName = entDistrictName;
    }

    public Long getProductSize() {
        return productSize;
    }

    public void setProductSize(Long productSize) {
        this.productSize = productSize;
    }

    public String getCanOperate() {
        return canOperate;
    }

    public void setCanOperate(String canOperate) {
        this.canOperate = canOperate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
    
    

}