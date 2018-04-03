package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 企业扩展信息
 */
public class EnterprisesExtInfo {
    private Long id;

    private Long enterId;

    private String ecCode;

    private String ecPrdCode;

    private String feature;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    private Integer joinType; //广东众筹，企业参与方式，1、大规模企业；2、中小规模企业

    private String callbackUrl; //广东众筹回调url
    
    private Integer abilityConfig;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnterId() {
        return enterId;
    }

    public void setEnterId(Long enterId) {
        this.enterId = enterId;
    }

    public String getEcCode() {
        return ecCode;
    }

    public void setEcCode(String ecCode) {
        this.ecCode = ecCode == null ? null : ecCode.trim();
    }

    public String getEcPrdCode() {
        return ecPrdCode;
    }

    public void setEcPrdCode(String ecPrdCode) {
        this.ecPrdCode = ecPrdCode == null ? null : ecPrdCode.trim();
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature == null ? null : feature.trim();
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

    public Integer getJoinType() {
        return joinType;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Integer getAbilityConfig() {
        return abilityConfig;
    }

    public void setAbilityConfig(Integer abilityConfig) {
        this.abilityConfig = abilityConfig;
    }
}