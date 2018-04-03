package com.cmcc.vrp.province.module;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 四川单点登录请求响应对象中的具体业务参数
 * ScUserInfoRespData.java
 * @author wujiamin
 * @date 2017年4月17日
 */
public class ScUserInfoRespData {
    @JSONField(name="Mobile")
    private String mobile;
    
    @JSONField(name="UID")
    private String uid;
    
    @JSONField(name="City")
    private String city;
    
    @JSONField(name="Lng")
    private String lng;
        
    @JSONField(name="Lat")
    private String lat;
    
    @JSONField(name="IsScMobile")
    private String isScMobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getIsScMobile() {
        return isScMobile;
    }

    public void setIsScMobile(String isScMobile) {
        this.isScMobile = isScMobile;
    }
}
