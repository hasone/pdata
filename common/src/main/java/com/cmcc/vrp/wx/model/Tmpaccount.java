package com.cmcc.vrp.wx.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Tmpaccount.java
 * @author wujiamin
 * @date 2017年3月30日
 */
public class Tmpaccount {
    private String openid;

    private BigDecimal count;

    private Date createTime;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}