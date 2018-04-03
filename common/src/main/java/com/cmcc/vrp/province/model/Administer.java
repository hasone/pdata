package com.cmcc.vrp.province.model;

import com.cmcc.vrp.exception.SelfCheckException;
import com.cmcc.vrp.util.PropertyValidator;

import java.util.Date;

/**
 * 
 * @ClassName: Administer 
 * @Description: TODO
 */
public class Administer {
    public final static String DEFAULT_VALUE = "-127";
    private Long id;
    private String userName;
    private String password;
    private String passwordNew;
    private String salt;
    private String mobilePhone;
    private Long creatorId;
    private Long roleId;
    private String picUrl;
    private String email;
    private Date createTime;
    private Date updateTime;
    private Integer deleteFlag;
    private String citys;
    private String roleName;
    private String managerName;
    private Long managerId;
    private Long parentManagerId;
    private Date passwordUpdateTime;
    //以下为扩展属性
    private String enterpriseCode;//企业编码
    
    private String description; //用于描述企业当前审核状态
    
    private Integer adminChangeStatus;//用户变更状态
    
    private String districtName;

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
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

    public String getCitys() {
        return citys;
    }

    public void setCitys(String citys) {
        this.citys = citys == null ? null : citys.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * @return
     * @throws SelfCheckException
     * @throws
     * @Title:selfCheck
     * @Description: 用户的检查
     * @author: sunyiwei
     */
    public boolean selfCheck() throws SelfCheckException {
        /**
         * 检查姓名没有特殊字符且长度不大于64
         */

        PropertyValidator.isChineseLowerNumberUnderline(userName, "姓名称");
        PropertyValidator.maxLengthCheck(userName, 64, "姓名称");

        /**
         * 密码必须包含字母、数字、特殊符号且长度为10到20位
         */
        PropertyValidator.isStrictPwd(password, "用户密码");

        //检查phone是否是电话格式
        PropertyValidator.isPhoneNum(mobilePhone, "用户手机号码");

        /**
         * 邮箱地址
         */
        PropertyValidator.isEmail(email, "邮箱地址");
        PropertyValidator.maxLengthCheck(email, 64, "邮箱地址");

        //检验通过返回true
        return true;

    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Long getParentManagerId() {
        return parentManagerId;
    }

    public void setParentManagerId(Long parentManagerId) {
        this.parentManagerId = parentManagerId;
    }

    public Date getPasswordUpdateTime() {
        return passwordUpdateTime;
    }

    public void setPasswordUpdateTime(Date passwordUpdateTime) {
        this.passwordUpdateTime = passwordUpdateTime;
    }

    public String getEnterpriseCode() {
	return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
	this.enterpriseCode = enterpriseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAdminChangeStatus() {
        return adminChangeStatus;
    }

    public void setAdminChangeStatus(Integer adminChangeStatus) {
        this.adminChangeStatus = adminChangeStatus;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

}
