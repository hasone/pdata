package com.cmcc.vrp.province.model;

import java.util.Date;

public class ChangeMobileRecord {
    private Long id;

    private Long adminId;

    private String oldMobile;

    private String newMobile;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getOldMobile() {
        return oldMobile;
    }

    public void setOldMobile(String oldMobile) {
        this.oldMobile = oldMobile == null ? null : oldMobile.trim();
    }

    public String getNewMobile() {
        return newMobile;
    }

    public void setNewMobile(String newMobile) {
        this.newMobile = newMobile == null ? null : newMobile.trim();
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