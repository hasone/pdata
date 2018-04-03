package com.cmcc.vrp.province.model;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:01:54
*/
public class MdrcTemplate {
    private Long id;

    private String name;

    private String frontImage;

    private String rearImage;

    private Long productId;

    private String productSize;
    private Date createTime;
    private Integer deleteFlag;
    private Date deleteTime;
    private Long creatorId;
    //产品名称
    private String productName;
    //资源数量
    private int resourcesCount = 0;
    //主题
    private String theme;
    
    //模板类型：0:通用模板；1：自定义模板
    private Integer type;
    
    private String frontImageName;

    private String rearImageName;

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getResourcesCount() {
        return resourcesCount;
    }

    public void setResourcesCount(int resourcesCount) {
        this.resourcesCount = resourcesCount;
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

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public String getRearImage() {
        return rearImage;
    }

    public void setRearImage(String rearImage) {
        this.rearImage = rearImage;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFrontImageName() {
        return frontImageName;
    }

    public void setFrontImageName(String frontImageName) {
        this.frontImageName = frontImageName;
    }

    public String getRearImageName() {
        return rearImageName;
    }

    public void setRearImageName(String rearImageName) {
        this.rearImageName = rearImageName;
    }
    
}