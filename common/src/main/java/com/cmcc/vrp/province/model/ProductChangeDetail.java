package com.cmcc.vrp.province.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:06:28
*/
public class ProductChangeDetail {
    private Long id;

    private Long requestId;

    private Long productId;

    private Integer operate;

    private Integer discount;

    private Integer deleteFlag;
    
    private Long oldProductTemplateId; //旧产品模板id
    
    private Long newProductTemplateId; //新产品模板id
    
    //extended
    private String prdName;

    private String prdCode;

    private int price;
    
    private String isp; //产品运营商
    
    private Long productSize; //产品大小
    
    private String ownershipRegion; //
    
    private String roamingRegion;

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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getOperate() {
        return operate;
    }

    public void setOperate(Integer operate) {
        this.operate = operate;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getOldProductTemplateId() {
        return oldProductTemplateId;
    }

    public void setOldProductTemplateId(Long oldProductTemplateId) {
        this.oldProductTemplateId = oldProductTemplateId;
    }

    public Long getNewProductTemplateId() {
        return newProductTemplateId;
    }

    public void setNewProductTemplateId(Long newProductTemplateId) {
        this.newProductTemplateId = newProductTemplateId;
    }

    public String getPrdName() {
        return prdName;
    }

    public void setPrdName(String prdName) {
        this.prdName = prdName;
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public Long getProductSize() {
        return productSize;
    }

    public void setProductSize(Long productSize) {
        this.productSize = productSize;
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
    
    
}