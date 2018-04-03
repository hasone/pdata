package com.cmcc.vrp.boss.guangdongpool.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * 服务信息
 *
 * Created by sunyiwei on 2016/11/18.
 */
@XStreamAlias("Service")
public class Service {
    @XStreamAlias("ServiceCode")
    private String serviceCode;

    @XStreamAlias("Params")
    private List<Param> params;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }
}
