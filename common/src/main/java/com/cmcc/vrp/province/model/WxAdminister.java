package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 对于广东众筹平台
 * 微信用户和管理员分开管理
 * 避免微信侧更改手机号影响管理员账号，因此将微信用户单独拉出来放在一张新表
 * created by qinqinyan on 2017/09/19
 * */
public class WxAdminister {
    private Long id;

    private String userName;

    private String password;

    private String passwordNew;

    private String salt;

    private Date passwordUpdateTime;

    private String mobilePhone;

    private Long creatorId;

    private String picUrl;

    private String email;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    private String citys;

    private String userUuid;

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

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew == null ? null : passwordNew.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public Date getPasswordUpdateTime() {
        return passwordUpdateTime;
    }

    public void setPasswordUpdateTime(Date passwordUpdateTime) {
        this.passwordUpdateTime = passwordUpdateTime;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid == null ? null : userUuid.trim();
    }
}