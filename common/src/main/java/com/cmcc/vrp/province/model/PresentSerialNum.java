package com.cmcc.vrp.province.model;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:06:15
*/
public class PresentSerialNum {
    private Long id;

    private String blockSerialNum;

    private String platformSerialNum;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlockSerialNum() {
        return blockSerialNum;
    }

    public void setBlockSerialNum(String blockSerialNum) {
        this.blockSerialNum = blockSerialNum == null ? null : blockSerialNum.trim();
    }

    public String getPlatformSerialNum() {
        return platformSerialNum;
    }

    public void setPlatformSerialNum(String platformSerialNum) {
        this.platformSerialNum = platformSerialNum == null ? null : platformSerialNum.trim();
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