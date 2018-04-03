package com.cmcc.vrp.boss.guangdong.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/7/11.
 */
@XStreamAlias("PrdList")
public class PrdList1 {

    @XStreamAlias("PrdCode")
    private String prdCode;
    @XStreamAlias("OptType")
    String optType;
    @XStreamAlias("Service")
    private GdService1 service;

    public GdService1 getService() {
        return service;
    }

    public void setService(GdService1 service) {
        this.service = service;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }
}
