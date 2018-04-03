package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 * 2016年11月11日
 */
@XStreamAlias("EntInfo")
public class EntInfoRespData {
	
    @XStreamAlias("EnterpriseId")
	Long id;
	
    @XStreamAlias("AppKey")
	String appkey;
	
    @XStreamAlias("AppSecret")
	String appsecret;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }
}
