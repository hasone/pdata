package com.cmcc.vrp.province.model;

import java.util.Date;


/**
 * @author lgk8023
 * 运营商产品余额信息
 */
public class SupplierProductAccount {
	
    private Long id;
	
    private Long supplierProductId;
    
    private Long entSyncListId;
	
    private Double count;
	
    private Double minCount;
	
    private Date createTime;

    private Date updateTime;

    private int deleteFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierProductId() {
        return supplierProductId;
    }

    public void setSupplierProductId(Long supplierProductId) {
        this.supplierProductId = supplierProductId;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
    	this.count = count;
    }

    public Double getMinCount() {
        return minCount;
    }

    public void setMinCount(Double minCount) {
        this.minCount = minCount;
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

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
	public String toString() {
        return "SupplierProductAccount [id=" + id + ", supplierProductId=" + supplierProductId + ", count=" + count
				+ ", minCount=" + minCount + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", deleteFlag=" + deleteFlag + "]";
    }

    public Long getEntSyncListId() {
    	return entSyncListId;
    }

    public void setEntSyncListId(Long entSyncListId) {
        this.entSyncListId = entSyncListId;
    }   
}
