package com.cmcc.vrp.boss.jiangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * @author lgk8023
 *
 */
@XStreamAlias("HEAD")
public class JxQueryHead {

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
