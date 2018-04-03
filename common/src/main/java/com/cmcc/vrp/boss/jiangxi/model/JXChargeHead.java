package com.cmcc.vrp.boss.jiangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 江西渠道充值请求HEAD
 * Created by leelyn on 2016/7/7.
 */
@XStreamAlias("HEAD")
public class JXChargeHead {

    @XStreamAlias("CODE")
    private String code;
    @XStreamAlias("SID")
    private String sid;
    @XStreamAlias("TIMESTAMP")
    private String timeStmp;
    @XStreamAlias("SERVICEID")
    private String serviceId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTimeStmp() {
        return timeStmp;
    }

    public void setTimeStmp(String timeStmp) {
        this.timeStmp = timeStmp;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
