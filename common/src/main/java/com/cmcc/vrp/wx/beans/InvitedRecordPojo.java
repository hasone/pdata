package com.cmcc.vrp.wx.beans;

/**
 * 微信发送的被邀请记录
 * InvitedRecordPojo.java
 * @author wujiamin
 * @date 2017年2月28日
 */
public class InvitedRecordPojo {
    private String openid;

    private String mobile;

    private String adminId;

    private String ticket;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
