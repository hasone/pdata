package com.cmcc.vrp.province.model;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:05:46
*/
public class PresentRule {
    private Long id;

    private Long entId;

    private String eName;

    private String aName;//活动名称

    private Integer total;

    private Byte status;

    private Date createTime;

    private Date updateTime;

    private Long creatorId;

    private Long updaterId;

    private int deleteFlag;

    private String deleteFlagVo = "1";

    private Byte version;

    private String eCode;

    private String pCode;

    private String ruleId;

    //用于下发短信

    private Integer useSms;//开启短信功能标志

    private Long smsTemplateId;//短信模板id
    
    /**
     * 拓展activityCreator
     */
    private ActivityCreator activityCreator;

    public Integer getUseSms() {
        return useSms;
    }

    public void setUseSms(Integer useSms) {
        this.useSms = useSms;
    }

    public Long getSmsTemplateId() {
        return smsTemplateId;
    }

    public void setSmsTemplateId(Long smsTemplateId) {
        this.smsTemplateId = smsTemplateId;
    }

    public String getDeleteFlagVo() {
        return deleteFlag + "";
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String geteCode() {
        return eCode;
    }

    public void seteCode(String eCode) {
        this.eCode = eCode;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public ActivityCreator getActivityCreator() {
        return activityCreator;
    }

    public void setActivityCreator(ActivityCreator activityCreator) {
        this.activityCreator = activityCreator;
    }
    
    
}