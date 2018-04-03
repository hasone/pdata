package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/13.
 */
public class UserData {

    @XStreamAlias("MobNum")
    private String mobNum;
    @XStreamAlias("OprCode")
    private String oprCode;
    @XStreamAlias("UserPackage")
    private String userPackage;
    @XStreamAlias("UsageLimit")
    private Integer usageLimit;
    @XStreamAlias("ValidMonths")
    private String validMonths;

    public String getMobNum() {
        return mobNum;
    }

    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }

    public String getOprCode() {
        return oprCode;
    }

    public void setOprCode(String oprCode) {
        this.oprCode = oprCode;
    }

    public String getUserPackage() {
        return userPackage;
    }

    public void setUserPackage(String userPackage) {
        this.userPackage = userPackage;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public String getValidMonths() {
        return validMonths;
    }

    public void setValidMonths(String validMonths) {
        this.validMonths = validMonths;
    }
}
