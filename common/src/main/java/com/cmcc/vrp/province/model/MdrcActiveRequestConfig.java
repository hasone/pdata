package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 激活操作：审核记录和卡数据关系类
 * */
public class MdrcActiveRequestConfig {
    private Long id;

    private Long requestId;

    private Long configId;

    private String startSerial;

    private String endSerial;

    private Date createTime;

    private Integer deleteFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getStartSerial() {
        return startSerial;
    }

    public void setStartSerial(String startSerial) {
        this.startSerial = startSerial == null ? null : startSerial.trim();
    }

    public String getEndSerial() {
        return endSerial;
    }

    public void setEndSerial(String endSerial) {
        this.endSerial = endSerial == null ? null : endSerial.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}