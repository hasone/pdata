package com.cmcc.vrp.boss.jiangsu.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("item")
public class Item {
    @XStreamAlias("ordertime")
    private String ordertime;
    @XStreamAlias("orderid")
    private String orderid;
    @XStreamAlias("orderstatus")
    private String orderstatus;
    @XStreamAlias("orderchannelid")
    private String orderchannelid;

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getOrderchannelid() {
        return orderchannelid;
    }

    public void setOrderchannelid(String orderchannelid) {
        this.orderchannelid = orderchannelid;
    }
}
