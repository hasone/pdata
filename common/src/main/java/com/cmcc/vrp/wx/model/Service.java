package com.cmcc.vrp.wx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月5日 上午8:54:59
*/
@XStreamAlias("Service")
public class Service {
    @XStreamAlias("ServiceCode")
    private String serviceCode;

    @XStreamAlias("UserInfoMap")
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
