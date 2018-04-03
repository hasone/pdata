package com.cmcc.vrp.boss.bjym.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * 北京云漫渠道的用户数据
 *
 * Created by sunyiwei on 2017/4/6.
 */
public class BjymUserData {
    @SerializedName("mobiles")
    private String mobile;
    private String userPackage;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserPackage() {
        return userPackage;
    }

    public void setUserPackage(String userPackage) {
        this.userPackage = userPackage;
    }
}
