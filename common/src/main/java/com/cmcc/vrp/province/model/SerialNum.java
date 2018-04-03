package com.cmcc.vrp.province.model;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:08:37
*/
public class SerialNum {
    private Long id;

    private String platformSerialNum;

    private String ecSerialNum;

    private String bossReqSerialNum;

    private String bossRespSerialNum;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlatformSerialNum() {
        return platformSerialNum;
    }

    public void setPlatformSerialNum(String platformSerialNum) {
        this.platformSerialNum = platformSerialNum == null ? null : platformSerialNum.trim();
    }

    public String getEcSerialNum() {
        return ecSerialNum;
    }

    public void setEcSerialNum(String ecSerialNum) {
        this.ecSerialNum = ecSerialNum == null ? null : ecSerialNum.trim();
    }

    public String getBossReqSerialNum() {
        return bossReqSerialNum;
    }

    public void setBossReqSerialNum(String bossReqSerialNum) {
        this.bossReqSerialNum = bossReqSerialNum == null ? null : bossReqSerialNum.trim();
    }

    public String getBossRespSerialNum() {
        return bossRespSerialNum;
    }

    public void setBossRespSerialNum(String bossRespSerialNum) {
        this.bossRespSerialNum = bossRespSerialNum == null ? null : bossRespSerialNum.trim();
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