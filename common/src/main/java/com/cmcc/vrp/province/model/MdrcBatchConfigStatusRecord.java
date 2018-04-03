package com.cmcc.vrp.province.model;

import java.util.Date;

public class MdrcBatchConfigStatusRecord {

    private Long id;

    private Long configId;

    private Integer preStatus;

    private Integer nowStatus;

    private Date createTime;

    private Date updateTime;

    private Long updatorId;

    private Integer deleteFlag;

    /**
     * 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     */
    public Long getConfigId() {
        return configId;
    }

    /**
     * 
     */
    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    /**
     * 
     */
    public Integer getPreStatus() {
        return preStatus;
    }

    /**
     * 
     */
    public void setPreStatus(Integer preStatus) {
        this.preStatus = preStatus;
    }

    /**
     * 
     */
    public Integer getNowStatus() {
        return nowStatus;
    }

    /**
     * 
     */
    public void setNowStatus(Integer nowStatus) {
        this.nowStatus = nowStatus;
    }

    /**
     * 
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 
     */
    public Long getUpdatorId() {
        return updatorId;
    }

    /**
     * 
     */
    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    /**
     * 
     */
    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * 
     */
    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}