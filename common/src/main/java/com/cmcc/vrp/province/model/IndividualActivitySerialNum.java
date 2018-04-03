package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * IndividualActivitySerialNum
 *
 */
public class IndividualActivitySerialNum {
    private Integer id;

    private String systemNum;

    private String ecSerialNum;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum == null ? null : systemNum.trim();
    }

    public String getEcSerialNum() {
        return ecSerialNum;
    }

    public void setEcSerialNum(String ecSerialNum) {
        this.ecSerialNum = ecSerialNum == null ? null : ecSerialNum.trim();
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