package com.cmcc.vrp.province.model;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:43:37
*/
public class ApprovalProcessDefinition {
    private Long id;

    private Long originRoleId;

    private Integer stage;

    private Integer targetStatus;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    private Integer type;
    
    private Integer msg;
    
    private Integer recvmsg;

    //extends
    private String roleName;

    private Long firstRoleId;

    private Long secondRoleId;

    private Long thirdRoleId;

    private Long forthRoleId;

    private Long fifthRoleId;

    public Long getFirstRoleId() {
        return firstRoleId;
    }

    public void setFirstRoleId(Long firstRoleId) {
        this.firstRoleId = firstRoleId;
    }

    public Long getSecondRoleId() {
        return secondRoleId;
    }

    public void setSecondRoleId(Long secondRoleId) {
        this.secondRoleId = secondRoleId;
    }

    public Long getThirdRoleId() {
        return thirdRoleId;
    }

    public void setThirdRoleId(Long thirdRoleId) {
        this.thirdRoleId = thirdRoleId;
    }

    public Long getForthRoleId() {
        return forthRoleId;
    }

    public void setForthRoleId(Long forthRoleId) {
        this.forthRoleId = forthRoleId;
    }

    public Long getFifthRoleId() {
        return fifthRoleId;
    }

    public void setFifthRoleId(Long fifthRoleId) {
        this.fifthRoleId = fifthRoleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginRoleId() {
        return originRoleId;
    }

    public void setOriginRoleId(Long originRoleId) {
        this.originRoleId = originRoleId;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(Integer targetStatus) {
        this.targetStatus = targetStatus;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMsg() {
        return msg;
    }

    public void setMsg(Integer msg) {
        this.msg = msg;
    }

    public Integer getRecvmsg() {
        return recvmsg;
    }

    public void setRecvmsg(Integer recvmsg) {
        this.recvmsg = recvmsg;
    }
    
    
}