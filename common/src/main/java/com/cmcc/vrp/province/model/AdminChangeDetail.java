package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 
 * 用户修改保存
 *
 */
public class AdminChangeDetail {
    private Long id;

    private Long requestId;

    private Long enterId;

    private Long adminId;

    private String srcName;

    private String srcPhone;

    private String destName;

    private String destPhone;

    private String comment;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;
    
    //以下为拓展字段，用于显示详情
    private String creatorName; //创建者姓名
    
    private String enterName; //企业名称
    
    private String districtName; //地区名称
    
    private Integer virifyStatus;//审核状态  0审核中  1.审核通过  2.审核驳回
    
    private Date requestTime; //请求时间

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

    public Long getEnterId() {
        return enterId;
    }

    public void setEnterId(Long enterId) {
        this.enterId = enterId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName == null ? null : srcName.trim();
    }

    public String getSrcPhone() {
        return srcPhone;
    }

    public void setSrcPhone(String srcPhone) {
        this.srcPhone = srcPhone == null ? null : srcPhone.trim();
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName == null ? null : destName.trim();
    }

    public String getDestPhone() {
        return destPhone;
    }

    public void setDestPhone(String destPhone) {
        this.destPhone = destPhone == null ? null : destPhone.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getEnterName() {
        return enterName;
    }

    public void setEnterName(String enterName) {
        this.enterName = enterName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Integer getVirifyStatus() {
        return virifyStatus;
    }

    public void setVirifyStatus(Integer virifyStatus) {
        this.virifyStatus = virifyStatus;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }
    
    
}