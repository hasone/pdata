package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/18.
 */
@XStreamAlias("InterBOSS")
public class GxChargeResp {

    @XStreamAlias("Version")
    private String version;
    @XStreamAlias("TestFlag")
    private String testFlag;
    @XStreamAlias("BIPType")
    private BIPType bIPType;
    @XStreamAlias("RoutingInfo")
    private RoutingInfo routingInfo;
    @XStreamAlias("TransInfo")
    private TransInfoResp transInfo;
    @XStreamAlias("SNReserve")
    private SNReserve sNReserve;
    @XStreamAlias("Response")
    private Response response;
    @XStreamAlias("SvcCont")
    private String svcCont;


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

    public TransInfoResp getTransInfo() {
        return transInfo;
    }

    public void setTransInfo(TransInfoResp transInfo) {
        this.transInfo = transInfo;
    }

    /**
     * @return
     */
    public SNReserve getsNReserve() {
        return sNReserve;
    }

    /**
     * @param sNReserve
     */
    public void setsNReserve(SNReserve sNReserve) {
        this.sNReserve = sNReserve;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getSvcCont() {
        return svcCont;
    }

    public void setSvcCont(String svcCont) {
        this.svcCont = svcCont;
    }
}
