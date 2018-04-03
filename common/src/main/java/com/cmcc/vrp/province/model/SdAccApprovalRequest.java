package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 山东充值记录导出记录表
 *
 */
public class SdAccApprovalRequest {
    private Long id;

    private Long processId;
    
    private Integer status;
    
    private Date createTime;
    
    private Integer deleteFlag;
    
    private Double count;
    
    private Long entId;
    
    private String entName;

    private String entCode;
    
    private Integer discountType;
    
    private Integer discountValue;
    
    private Integer productType; //产品类型

    private String productTypeName;//产品类型名称
    
    private String pkgSeq;
    
    private String acctSeq;
    
    private String acctId;
    
    //extend  
    private String description; //状态描述
    
    private String districtName;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
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

    public String getPkgSeq() {
        return pkgSeq;
    }

    public void setPkgSeq(String pkgSeq) {
        this.pkgSeq = pkgSeq;
    }

    public String getAcctSeq() {
        return acctSeq;
    }

    public void setAcctSeq(String acctSeq) {
        this.acctSeq = acctSeq;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    
    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    /**
     * 转化为ApprovalRequest
     */
    public ApprovalRequest convertToApprovalRequest(){
        ApprovalRequest request = new ApprovalRequest();
        request.setId(id);
        request.setProcessId(processId);
        request.setDeleteFlag(deleteFlag);
        request.setStatus(status);
        request.setEntId(entId);
        return request;
    }
}
