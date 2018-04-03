package com.cmcc.vrp.province.model;

import java.util.Date;
/**
 * 签到详细记录对象
 * */
public class WxSignDetailRecord {
    private Long id;

    private Long adminId;

    private Date signTime;

    private Integer coinCount;

    private Long serailSignId;

    private Date updateTime;

    private Integer deleteFlag;
    
    private String serialNum;
    
    private Integer type; //0:签到；1：签到奖励

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

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Integer getCoinCount() {
        return coinCount;
    }

    public void setCoinCount(Integer coinCount) {
        this.coinCount = coinCount;
    }

    public Long getSerailSignId() {
        return serailSignId;
    }

    public void setSerailSignId(Long serailSignId) {
        this.serailSignId = serailSignId;
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

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}