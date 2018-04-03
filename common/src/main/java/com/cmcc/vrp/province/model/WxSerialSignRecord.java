package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 广东众筹连续签到对象
 * */
public class WxSerialSignRecord {
    private Long id;

    private Long adminId;

    private Date startTime;

    private Integer count;

    private Date updateTime;

    private Integer deleteFlag;
    
    private Integer serialFlag;
    
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public Integer getSerialFlag() {
        return serialFlag;
    }

    public void setSerialFlag(Integer serialFlag) {
        this.serialFlag = serialFlag;
    }

}