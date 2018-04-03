package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("Request")
public class MemOperateReq {

    @XStreamAlias("Datetime")
    private String datetime;
    
    @XStreamAlias("Info")
    private MemOperateInfo memOperateInfo;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public MemOperateInfo getMemOperateInfo() {
        return memOperateInfo;
    }

    public void setMemOperateInfo(MemOperateInfo memOperateInfo) {
        this.memOperateInfo = memOperateInfo;
    }
}
