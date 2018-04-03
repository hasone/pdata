/**
 * 
 */
package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 *  @desc: 企业黑名单
 *  @author: wuguoping 
 *  @data: 2017年7月13日
 */
public class EnterpriseBlacklist {
    private Long id;
    
    private String enterpriseName;
    
    private String keyName;
    
    private Long creatorId;
    
    private Date createrTime;
    
    private Date updateTime;
    
    private Integer deleteFlag;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the enterpriseName
     */
    public String getEnterpriseName() {
        return enterpriseName;
    }

    /**
     * @param enterpriseName the enterpriseName to set
     */
    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    /**
     * @return the keyName
     */
    public String getKeyName() {
        return keyName;
    }

    /**
     * @param keyName the keyName to set
     */
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    /**
     * @return the creatorId
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * @param creatorId the creatorId to set
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * @return the createrTime
     */
    public Date getCreaterTime() {
        return createrTime;
    }

    /**
     * @param createrTime the createrTime to set
     */
    public void setCreaterTime(Date createrTime) {
        this.createrTime = createrTime;
    }

    /**
     * @return the updateTime
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return the deleteFlag
     */
    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * @param deleteFlag the deleteFlag to set
     */
    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    
    
}
