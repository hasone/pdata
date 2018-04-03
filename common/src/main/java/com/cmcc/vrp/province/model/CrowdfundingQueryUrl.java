package com.cmcc.vrp.province.model;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月5日 上午10:01:09
*/
public class CrowdfundingQueryUrl {
    
    private Long id;
    
    private Long crowdfundingActivityDetailId;
    
    private String queryUrl;
    
    private Date createTime;

    private Date updateTime;
    
    private Integer deleteFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCrowdfundingActivityDetailId() {
        return crowdfundingActivityDetailId;
    }

    public void setCrowdfundingActivityDetailId(Long crowdfundingActivityDetailId) {
        this.crowdfundingActivityDetailId = crowdfundingActivityDetailId;
    }

    public String getQueryUrl() {
        return queryUrl;
    }

    public void setQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
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
