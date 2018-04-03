package com.cmcc.vrp.province.model;

import java.util.Date;

public class ProductTemplateEnterpriseMap {
    private Long id;

    private Long enterpriseId;

    private Long productTemplateId;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    //extended
    private String productTemplateName; //产品模板名称


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getProductTemplateId() {
        return productTemplateId;
    }

    public void setProductTemplateId(Long productTemplateId) {
        this.productTemplateId = productTemplateId;
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

    public String getProductTemplateName() {
        return productTemplateName;
    }

    public void setProductTemplateName(String productTemplateName) {
        this.productTemplateName = productTemplateName;
    }
}