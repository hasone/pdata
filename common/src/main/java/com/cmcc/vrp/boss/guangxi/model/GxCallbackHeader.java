package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/18.
 */
@XStreamAlias("InterBOSS")
public class GxCallbackHeader {

    @XStreamAlias("Version")
    private String version;
    @XStreamAlias("TestFlag")
    private String testFlag;
    @XStreamAlias("BIPType")
    private BIPType bIPType;
    @XStreamAlias("RoutingInfo")
    private RoutingInfo routingInfo;
    @XStreamAlias("TransInfo")
    private TransInfoReq transInfo;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTestFlag() {
        return testFlag;
    }

    public void setTestFlag(String testFlag) {
        this.testFlag = testFlag;
    }

    /**
     * @return
     */
    public BIPType getbIPType() {
        return bIPType;
    }

    /**
     * @param bIPType
     */
    public void setbIPType(BIPType bIPType) {
        this.bIPType = bIPType;
    }

    public RoutingInfo getRoutingInfo() {
        return routingInfo;
    }

    public void setRoutingInfo(RoutingInfo routingInfo) {
        this.routingInfo = routingInfo;
    }

    public TransInfoReq getTransInfo() {
        return transInfo;
    }

    public void setTransInfo(TransInfoReq transInfo) {
        this.transInfo = transInfo;
    }
}
