package com.cmcc.vrp.province.model;

import java.util.Date;

public class ProductTemplate {
    private Long id;

    private String name;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;
    
    private Integer defaultFlag; //1：企业默认关联该产品组；

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

    public Integer getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Integer defaultFlag) {
        this.defaultFlag = defaultFlag;
    }
}