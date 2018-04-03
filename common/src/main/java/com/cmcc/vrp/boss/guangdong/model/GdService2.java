package com.cmcc.vrp.boss.guangdong.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/7/11.
 */
@XStreamAlias("Service")
public class GdService2 {

    @XStreamAlias("ServiceCode")
    private String serviceCode;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}
