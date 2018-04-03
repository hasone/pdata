package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/18.
 */
@XStreamAlias("UserData")
public class UserDataReq {

    @XStreamAlias("MobNum")
    private String mobNum;
    @XStreamAlias("UserPackage")
    private String userPackage;

    public String getMobNum() {
        return mobNum;
    }

    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }

    public String getUserPackage() {
        return userPackage;
    }

    public void setUserPackage(String userPackage) {
        this.userPackage = userPackage;
    }
}
