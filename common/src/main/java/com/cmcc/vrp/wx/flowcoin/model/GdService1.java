package com.cmcc.vrp.wx.flowcoin.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/7/11.
 */
@XStreamAlias("Service")
public class GdService1 {

    @XStreamAlias("ServiceCode")
    private String serviceCode;

    @XStreamAlias("USERINFOMAP")
    private UserInfoMap userInfoMap;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public UserInfoMap getUserInfoMap() {
        return userInfoMap;
    }

    public void setUserInfoMap(UserInfoMap userInfoMap) {
        this.userInfoMap = userInfoMap;
    }
}
