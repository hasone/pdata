package com.cmcc.vrp.wx.beans;


/**
 * 微信公众号用户类型接口返回的对象
 * WxUserTypeInterfacePojo.java
 * @author wujiamin
 * @date 2017年2月22日
 */
public class WxUserTypeInterfacePojo {
    private Integer userType;
    
    private String mobile;
    
    private String openid;
    
    private String redirectUrl;

    public Integer getUserType() {
        return userType;
    }
    public void setUserType(Integer userType) {
        this.userType = userType;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getOpenid() {
        return openid;
    }
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    
    public String getRedirectUrl() {
        return redirectUrl;
    }
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
