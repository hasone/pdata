package com.cmcc.vrp.wx.flowcoin.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/7/11.
 */
@XStreamAlias("PrdList")
public class PrdList2 {

    @XStreamAlias("PrdCode")
    private String prdCode;

    @XStreamAlias("OptType")
    private String optType;

    @XStreamAlias("Service")
    private GdService2 service;

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public GdService2 getService() {
        return service;
    }

    public void setService(GdService2 service) {
        this.service = service;
    }
}
