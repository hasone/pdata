package com.cmcc.vrp.wx.beans;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 向微信公众号请求二维码的响应对象
 * InviteQrcodeResp.java
 * @author wujiamin
 * @date 2017年2月23日
 */
public class InviteQrcodeResp {
    @JSONField(name="ticket")
    private String ticket;//二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
    
    @JSONField(name="expire_seconds")
    private String expireSeconds; //该二维码有效时间，以秒为单位。 最大不超过2592000（即30天）。
   
    @JSONField(name="url")
    private String url; // 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(String expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
