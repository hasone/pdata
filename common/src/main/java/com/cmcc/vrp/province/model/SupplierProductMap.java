package com.cmcc.vrp.province.model;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:11:32
*/
public class SupplierProductMap {
    private Long id;

    private Long platformProductId;

    private Long supplierProductId;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;
    
    private Integer priorFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlatformProductId() {
        return platformProductId;
    }

    public void setPlatformProductId(Long platformProductId) {
        this.platformProductId = platformProductId;
    }

    public Long getSupplierProductId() {
        return supplierProductId;
    }

    public void setSupplierProductId(Long supplierProductId) {
        this.supplierProductId = supplierProductId;
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

    public Integer getPriorFlag() {
        return priorFlag;
    }

    public void setPriorFlag(Integer priorFlag) {
        this.priorFlag = priorFlag;
    }
}