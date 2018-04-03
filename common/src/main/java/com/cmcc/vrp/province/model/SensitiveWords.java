package com.cmcc.vrp.province.model;

import java.util.Date;

/**
* <p>Title: 敏感词</p>
* <p>Description: </p>
* @author lgk8023
* @date 2016年12月20日 上午10:40:43
*/
public class SensitiveWords {
    private Long id;
	
    private String name;
	
    private Long creatorId;
	
    private Date createTime;
	
    private Date updateTime;
    
    private String creatorName;
    
    private Integer deleteFlag;

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
        this.name = name;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
	public String toString() {
        return "SensitiveWords [id=" + id + ", name=" + name + ", creatorId=" + creatorId + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
    }
}
