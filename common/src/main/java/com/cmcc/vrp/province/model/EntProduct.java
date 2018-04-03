package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 企业产品关系类
 *
 * */
public class EntProduct {
    private Long id;

    private Long productId;

    private Long enterprizeId;

    private Integer deleteFlag;

    private Integer discount;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getEnterprizeId() {
        return enterprizeId;
    }

    public void setEnterprizeId(Long enterprizeId) {
        this.enterprizeId = enterprizeId;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
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
      
    @Override
    public boolean equals(Object obj) {  
        if(obj instanceof EntProduct){  
            EntProduct ep=(EntProduct) obj;  
            return (this.discount != null && this.productId != null 
                    && this.enterprizeId != null && this.deleteFlag != null
                    && this.discount.equals(ep.getDiscount())
                    && this.productId.equals(ep.getProductId())
                    && this.enterprizeId.equals(ep.getEnterprizeId())
                    && this.deleteFlag.equals(ep.getDeleteFlag()));  
        }else{  
            return super.equals(obj);  
              
        }  
    }
    
    @Override
    public int hashCode(){
        return super.hashCode();
    }
    
}