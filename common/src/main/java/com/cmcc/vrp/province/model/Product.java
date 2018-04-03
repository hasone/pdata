package com.cmcc.vrp.province.model;

import java.util.Date;

import com.cmcc.vrp.exception.SelfCheckException;
import com.cmcc.vrp.util.PropertyValidator;

/**
 * 产品类
 */
public class Product {
    private Long id;

    private String productCode;

    private Integer type;

    private String name;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    private Integer price;

    private Integer defaultvalue;

    private String isp;

    private String ownershipRegion;

    private String roamingRegion;

    private Long productSize;

    private Integer flowAccountFlag;

    private Long flowAccountProductId;
    
    private String illustration; //说明
    
    private Long productCustomerType; //产品分类id，具体类型与customer_type表关联

    //extend
    private Double count;//产品个数

    private Integer accountType; //账户类型

    private Integer discount; //折扣
    
    private Double preDiscount;//前端传输的double类型值，若10.0

    private Integer configurableNumFlag;
    
    private Integer supplierOpStatus;//订单状态
    
    private String productCustomerTypeName;//产品分类名称

    public Integer getConfigurableNumFlag() {
        return configurableNumFlag;
    }

    public void setConfigurableNumFlag(Integer configurableNumFlag) {
        this.configurableNumFlag = configurableNumFlag;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDefaultvalue() {
        return defaultvalue;
    }

    public void setDefaultvalue(Integer defaultvalue) {
        this.defaultvalue = defaultvalue;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp == null ? null : isp.trim();
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

    public Long getProductSize() {
        return productSize;
    }

    public void setProductSize(Long productSize) {
        this.productSize = productSize;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    /**
     * @return
     * @throws SelfCheckException
     * @throws
     * @Title:selfCheck
     * @Description: TODO
     * @author: xuwanlin
     */
    public boolean selfCheck() throws SelfCheckException {
        /**
         * 检查产品名称没有特殊字符且长度不大于64
         */
        PropertyValidator.maxLengthCheck(name, 64, "产品名称");

        /**
         * 检查产品编码没有特殊字符且长度不大于20
         */
        PropertyValidator.isNoContainChinese(productCode, "产品编码");
        PropertyValidator.maxLengthCheck(productCode, 64, "产品编码");

        //检验通过返回true
        return true;

    }

    public Integer getFlowAccountFlag() {
        return flowAccountFlag;
    }

    public void setFlowAccountFlag(Integer flowAccountFlag) {
        this.flowAccountFlag = flowAccountFlag;
    }

    public Long getFlowAccountProductId() {
        return flowAccountProductId;
    }

    public void setFlowAccountProductId(Long flowAccountProductId) {
        this.flowAccountProductId = flowAccountProductId;
    }

    public String getIllustration() {
        return illustration;
    }

    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }

    public Integer getSupplierOpStatus() {
        return supplierOpStatus;
    }

    public void setSupplierOpStatus(Integer supplierOpStatus) {
        this.supplierOpStatus = supplierOpStatus;
    }

    public Long getProductCustomerType() {
        return productCustomerType;
    }

    public void setProductCustomerType(Long productCustomerType) {
        this.productCustomerType = productCustomerType;
    }

    public String getProductCustomerTypeName() {
        return productCustomerTypeName;
    }

    public void setProductCustomerTypeName(String productCustomerTypeName) {
        this.productCustomerTypeName = productCustomerTypeName;
    }

    public Double getPreDiscount() {
        return preDiscount;
    }

    public void setPreDiscount(Double preDiscount) {
        this.preDiscount = preDiscount;
    }

}
