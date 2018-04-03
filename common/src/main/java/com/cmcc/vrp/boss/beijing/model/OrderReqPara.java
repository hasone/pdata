package com.cmcc.vrp.boss.beijing.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/26.
 */
@XStreamAlias("Params")
public class OrderReqPara {

    @XStreamAlias("CA")
    private String ca;

    @XStreamAlias("AdminName")
    private String adminName;

    @XStreamAlias("Password")
    private String passWord;

    @XStreamAlias("ResName")
    private String resName;

    @XStreamAlias("PhoneList")
    private String phoneList;

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(String phoneList) {
        this.phoneList = phoneList;
    }
}
