package com.cmcc.vrp.province.model;

import java.util.Date;


/**
 * 活动奖品信息类
 * */
public class ActivityPrize {
    private Long id;

    private String idPrefix;

    private String rankName;

    private Long enterpriseId;

    private Long productId;

    private String prizeName;//奖项名称,转化成流量包大小

    private Long count;

    private String probability;//当LotteryActivity中的probabilityType为奖0时，该值不为空；当LotteryActivity中的probabilityType为奖1时，该值为空；

    private String activityId;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    private Long size;  //流量总大小

    private Integer type; //流量类型，1为流量池产品，2为流量包产品 3为话费产品
    
    //private Integer discount;

    private Integer discount; //折扣，流量众筹必填字段，其他默认为-1

    //以下为扩展属性
    private String enterpriseCode;//企业编码

    private String enterpriseName;//企业名称

    private String productCode;//产品编码

    private String productName;//产品名称

    private Long productSize;//产品大小

    private String isp;//运营商

    private String ownershipRegion;//使用范围

    private String roamingRegion;//漫游范围

    private Long price;//售出价格

    private Integer flowType; //流量类型，产品属性之一

    //附加属性
    private Long balance;//可用余额


    //public Integer getDiscount() {
    //    return discount;
    //}

    //public void setDiscount(Integer discount) {
    //    this.discount = discount;
    //}

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public Integer getFlowType() {
        return flowType;
    }

    public void setFlowType(Integer flowType) {
        this.flowType = flowType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(String idPrefix) {
        this.idPrefix = idPrefix == null ? null : idPrefix.trim();
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName == null ? null : rankName.trim();
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId == null ? null : activityId.trim();
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOwnershipRegion() {
        return ownershipRegion;
    }

    public void setOwnershipRegion(String ownershipRegion) {
        this.ownershipRegion = ownershipRegion;
    }

    public String getRoamingRegion() {
        return roamingRegion;
    }

    public void setRoamingRegion(String roamingRegion) {
        this.roamingRegion = roamingRegion;
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

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }


}