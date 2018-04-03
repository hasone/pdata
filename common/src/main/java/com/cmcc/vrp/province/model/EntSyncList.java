package com.cmcc.vrp.province.model;

import java.util.Date;


/**
* <p>Title: </p>
* <p>Description: 企业同步产品列表</p>
* @author lgk8023
* @date 2016年11月30日 上午9:01:15
*/
public class EntSyncList {
	
    private Long id;
	
    private Long entId;
	
    private String entProductCode;
	
    private Integer status;
	
    private String syncInfo;
	
    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public String getEntProductCode() {
    	return entProductCode;
    }

    public void setEntProductCode(String entProductCode) {
    	this.entProductCode = entProductCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSyncInfo() {
        return syncInfo;
    }

    public void setSyncInfo(String syncInfo) {
    	this.syncInfo = syncInfo;
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

}
