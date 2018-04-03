package com.cmcc.vrp.boss.guangdongpool.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 服务属性信息
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
@XStreamAlias("Param")
public class Param {
    @XStreamAlias("ParamCode")
    private String paramCode;

    @XStreamAlias("ParamValue")
    private String paramValue;

    @XStreamAlias("OptType")
    private String optType;

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }
}
