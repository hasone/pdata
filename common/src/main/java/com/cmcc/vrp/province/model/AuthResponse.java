package com.cmcc.vrp.province.model;

/**
 * 微信鉴权对象
 * @author qinqinyan
 * */
public class AuthResponse {
    
    private Integer status; 
    
    private String wxOpenId;
    
    private String mobile;
    
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
