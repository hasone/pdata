package com.cmcc.vrp.wx.beans;

import java.util.Date;

/**
 * 微信发送的绑定消息pojo
 * BindMsgPojo.java
 * @author wujiamin
 * @date 2017年2月28日
 */
public class BindMsgPojo {
    private String openid;

    private String mobile;

    private Date createTime;

    private Date updateTime;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
