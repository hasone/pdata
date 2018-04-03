package com.cmcc.vrp.wx.model;

import java.util.Date;

/**
 * WxInviteRecord.java
 * @author wujiamin
 * @date 2017年3月2日
 */
public class WxInviteRecord {
    private Long id;

    private String inviteSerial;

    private Long inviteAdminId;

    private Long invitedAdminId;

    private String invitedOpenid;

    private String ticket;

    private Date inviteTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInviteSerial() {
        return inviteSerial;
    }

    public void setInviteSerial(String inviteSerial) {
        this.inviteSerial = inviteSerial == null ? null : inviteSerial.trim();
    }

    public Long getInviteAdminId() {
        return inviteAdminId;
    }

    public void setInviteAdminId(Long inviteAdminId) {
        this.inviteAdminId = inviteAdminId;
    }

    public Long getInvitedAdminId() {
        return invitedAdminId;
    }

    public void setInvitedAdminId(Long invitedAdminId) {
        this.invitedAdminId = invitedAdminId;
    }

    public String getInvitedOpenid() {
        return invitedOpenid;
    }

    public void setInvitedOpenid(String invitedOpenid) {
        this.invitedOpenid = invitedOpenid == null ? null : invitedOpenid.trim();
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket == null ? null : ticket.trim();
    }

    public Date getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(Date inviteTime) {
        this.inviteTime = inviteTime;
    }
}