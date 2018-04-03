package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 账户变更审批记录对象
 */
public class AccountChangeApprovalRecord {
    private Long id;

    private Long accountChangeRequestId;

    private Long operatorId;

    private String operatorComment;

    private String serialNum;

    private Integer operatorResult;

    private Date createTime;

    private Date updateTime;

    private Byte deleteFlag;

    private String roleName;

    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountChangeRequestId() {
        return accountChangeRequestId;
    }

    public void setAccountChangeRequestId(Long accountChangeRequestId) {
        this.accountChangeRequestId = accountChangeRequestId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorComment() {
        return operatorComment;
    }

    public void setOperatorComment(String operatorComment) {
        this.operatorComment = operatorComment == null ? null : operatorComment.trim();
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum == null ? null : serialNum.trim();
    }

    public Integer getOperatorResult() {
        return operatorResult;
    }

    public void setOperatorResult(Integer operatorResult) {
        this.operatorResult = operatorResult;
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

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}