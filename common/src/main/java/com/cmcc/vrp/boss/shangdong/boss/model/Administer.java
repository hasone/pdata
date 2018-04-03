package com.cmcc.vrp.boss.shangdong.boss.model;

import java.util.Date;

/** 
 * @ClassName:	Administer 
 * @Description:  账号类
 * @author:	panxin
 * @date:	2016年11月12日
 *  
 */
public class Administer {
    private Long id;

    private String userName;

    private String password;

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
    
    private String enterName;
    
    public final  static String  DEFAULT_VALUE="-127";
     
    /*
     * 云平台用户id（云平台用户唯一标识）
     */
    private String code;
    
    /*
     * 客户经理角色云平台唯一标识
     */
    private String managerCode;
       
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
        if(createTime != null){
            return (Date) createTime.clone();
        }else{
            return null;
        }
    }

    public void setCreateTime(Date createTime) {
        if(createTime != null){
            this.createTime = (Date) createTime.clone();
        }else{
            this.createTime = null;
        }
    }

    public Date getUpdateTime() {
        if(updateTime != null){
            return (Date) updateTime.clone();
        }else{
            return null;
        }
    }

    public void setUpdateTime(Date updateTime) {
        if(updateTime != null){
            this.updateTime = (Date) updateTime.clone();
        }else{
            this.updateTime = null;
        }
    }

    public Integer getDeleteFlag() {
        return this.deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCitys() {
        return this.citys;
    }

    public void setCitys(String citys) {
        this.citys = citys == null ? null : citys.trim();
    }

    public String getRoleName() {
    	return this.roleName;
    }

    public void setRoleName(String roleName) {
    	this.roleName = roleName;
    }

    public String getCode() {
    	return this.code;
    }

    public void setCode(String code) {
    	this.code = code;
    }

    public String getManagerCode() {
    	return this.managerCode;
    }

    public void setManagerCode(String managerCode) {
    	this.managerCode = managerCode;
    }

    public String getEnterName() {
    	return enterName;
    }

    public void setEnterName(String enterName) {
    	this.enterName = enterName;
    }
}
