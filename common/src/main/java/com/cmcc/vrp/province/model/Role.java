package com.cmcc.vrp.province.model;


import com.cmcc.vrp.exception.SelfCheckException;
import com.cmcc.vrp.util.PropertyValidator;

import java.util.Date;


/**
 * 角色类
 * */
public class Role {
    private Long roleId;

    private String name;

    private String code; // 角色代码，唯一标识

    private String description;

    private Date createTime;

    private Long creator;

    private Long updateUser;

    private Date updateTime;

    private Integer deleteFlag;

    private Integer roleStatus;

    private Integer canBeDeleted; // 标记该角色是否可被删除， 1代表可以，0代表不可以

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
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

    public Integer getRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(Integer roleStatus) {
        this.roleStatus = roleStatus;
    }

    /**
     * @Title: getCode
     * @Description: TODO
     * @return: String
     */
    public String getCode() {
        return code;
    }

    /**
     * @Title: setCode
     * @Description: TODO
     * @return: void
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @Title: getCanBeDeleted
     * @Description: TODO
     * @return: Integer
     */
    public Integer getCanBeDeleted() {
        return canBeDeleted;
    }

    /**
     * @Title: setCanBeDeleted
     * @Description: TODO
     * @return: void
     */
    public void setCanBeDeleted(Integer canBeDeleted) {
        this.canBeDeleted = canBeDeleted;
    }

    /**
     * @return
     * @throws SelfCheckException
     * @throws
     * @Title:selfCheck
     * @Description: 角色对象的有效性检查
     * @author: sunyiwei
     */
    public boolean selfCheck() throws SelfCheckException {
        /**
         * 角色名称： 非空，最长不超过20，只能包含中文、大小写、数字和下划线
         */
        String szRoleNameProperty = "角色名称";
        PropertyValidator.isBlank(name, szRoleNameProperty);
        PropertyValidator.maxLengthCheck(name, 20, szRoleNameProperty);
        PropertyValidator.isChineseLowerNumberUnderline(name, szRoleNameProperty);

        /**
         * 角色代码：非空， 最长不超过18， 只能包含数字
         */
        String szRoleCodeProperty = "角色代码";
        PropertyValidator.isBlank(code, szRoleCodeProperty);
        PropertyValidator.maxLengthCheck(code, 18, szRoleCodeProperty);
        PropertyValidator.isNumberNonNegative(code, szRoleCodeProperty);

        return true;
    }
}
