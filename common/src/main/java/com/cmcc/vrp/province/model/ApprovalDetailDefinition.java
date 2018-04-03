package com.cmcc.vrp.province.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:43:22
*/
public class ApprovalDetailDefinition {
    private Long id;

    private Long processId;

    private Long roleId;

    private Integer approvalCode;

    private Integer precondition;

    private Integer deleteFlag;

    //extended
    private String roleName;


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

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(Integer approvalCode) {
        this.approvalCode = approvalCode;
    }

    public Integer getPrecondition() {
        return precondition;
    }

    public void setPrecondition(Integer precondition) {
        this.precondition = precondition;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}