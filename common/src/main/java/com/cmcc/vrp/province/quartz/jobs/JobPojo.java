package com.cmcc.vrp.province.quartz.jobs;

import java.io.Serializable;

/**
 * 存放定时任务参数
 * 支持序列化与反序列化JSON各式
 *
 * @author zhoujianbin
 */
public class JobPojo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 276261984834630857L;

    private Long ruleId;

    private Integer serialNumber;

    //最后一批
    private Boolean isLastGroup;

    public JobPojo() {
    }

    public JobPojo(Long ruleId, Integer serialNumber, Boolean isLastGroup) {
        this.ruleId = ruleId;
        this.serialNumber = serialNumber;
        this.isLastGroup = isLastGroup;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Boolean getIsLastGroup() {
        return isLastGroup;
    }

    public void setIsLastGroup(Boolean isLastGroup) {
        this.isLastGroup = isLastGroup;
    }
}