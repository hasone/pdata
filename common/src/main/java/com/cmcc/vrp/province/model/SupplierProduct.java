package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 
 * @ClassName: SupplierProduct 
 * @Description: TODO
 */
public class SupplierProduct {
    private Long id;

    private Long supplierId;

    private String name;

    private String isp;

    private String code;

    private Long size;

    private String ownershipRegion;

    private String roamingRegion;

    private Integer price;

    private String feature;//供应商定义的产品相关的信息，用JSON字符串表达

    private Integer status; //上下架状态，1上架，0下架

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;
    
    private String illustration; //说明
    
    private Double limitMoney; //限额,单位分，-1则不限额
    
    private Integer limitMoneyFlag; //全量限额标志位，0：不开启；1:开启
    
    private String opType;//订单操作说明：01订购；02删除操作；03变更操作；04暂停操作；05恢复操作
    
    private Integer opStatus;//订单操作结果说明：0正常状态；1删除状态；2暂停状态
    
    //供应商产品也需要区分产品类型，为了显示用
    private Integer type; //产品类型：0为现金产品，1为流量池产品，2为流量包产品，3为话费产品，4虚拟币产品
    
    //extend
    private String supplierName;

    private Integer supplierProductPrice;

    private Long supplierProductSize;
    
    private Integer discount;
    
    private String enterpriseCode;//企业编码
    
    private Integer supplierStatus; //供应商上下架状态
    
    private Double leftCount; //產品餘額
    
    private Double supplierLimitMoney; //供应商限额额度
    
    private Integer supplierLimitMoneyFlag; //供应商限额标志位
    
    private Integer productType; //产品类型：0为现金产品，1为流量池产品，2为流量包产品，3为话费产品，4虚拟币产品

    public Integer getSupplierProductPrice() {
        return supplierProductPrice;
    }

    public void setSupplierProductPrice(Integer supplierProductPrice) {
        this.supplierProductPrice = supplierProductPrice;
    }

    public Long getSupplierProductSize() {
        return supplierProductSize;
    }

    public void setSupplierProductSize(Long supplierProductSize) {
        this.supplierProductSize = supplierProductSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getOwnershipRegion() {
        return ownershipRegion;
    }

    public void setOwnershipRegion(String ownershipRegion) {
        this.ownershipRegion = ownershipRegion == null ? null : ownershipRegion.trim();
    }

    public String getRoamingRegion() {
        return roamingRegion;
    }

    public void setRoamingRegion(String roamingRegion) {
        this.roamingRegion = roamingRegion == null ? null : roamingRegion.trim();
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature == null ? null : feature.trim();
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

    public String getIllustration() {
		return illustration;
	}

	public void setIllustration(String illustration) {
		this.illustration = illustration;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public Integer getSupplierStatus() {
        return supplierStatus;
    }

    public void setSupplierStatus(Integer supplierStatus) {
        this.supplierStatus = supplierStatus;
    }

    public Double getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(Double leftCount) {
        this.leftCount = leftCount;
    }

    public Double getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(Double limitMoney) {
        this.limitMoney = limitMoney;
    }

    public Integer getLimitMoneyFlag() {
        return limitMoneyFlag;
    }

    public void setLimitMoneyFlag(Integer limitMoneyFlag) {
        this.limitMoneyFlag = limitMoneyFlag;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getSupplierLimitMoney() {
        return supplierLimitMoney;
    }

    public void setSupplierLimitMoney(Double supplierLimitMoney) {
        this.supplierLimitMoney = supplierLimitMoney;
    }

    public Integer getSupplierLimitMoneyFlag() {
        return supplierLimitMoneyFlag;
    }

    public void setSupplierLimitMoneyFlag(Integer supplierLimitMoneyFlag) {
        this.supplierLimitMoneyFlag = supplierLimitMoneyFlag;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public Integer getOpStatus() {
        return opStatus;
    }

    public void setOpStatus(Integer opStatus) {
        this.opStatus = opStatus;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

}