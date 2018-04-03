package com.cmcc.vrp.boss.beijing.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/8/11.
 */
@XStreamAlias("PhoneList")
public class PhoneList {

    @XStreamAlias("PhoneNumber")
    private String phoneNumber;
    @XStreamAlias("State")
    private String state;
    @XStreamAlias("ResName")
    private String resName;
    @XStreamAlias("Detail")
    private String detail;
    @XStreamAlias("CreateTime")
    private String createTime;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
