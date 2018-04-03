package com.cmcc.vrp.boss.hebei.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/8/19.
 */
@XStreamAlias("InterBOSS")
public class InterBOSSResp {

    @XStreamAlias("OrigDomain")
    private String OrigDomain;
    @XStreamAlias("HomeDomain")
    private String HomeDomain;
    @XStreamAlias("BIPCode")
    private String BIPCode;
    @XStreamAlias("ActivityCode")
    private String ActivityCode;
    @XStreamAlias("ActionCode")
    private String ActionCode;
    @XStreamAlias("ProcID")
    private String ProcID;
    @XStreamAlias("TransIDO")
    private String TransIDO;
    @XStreamAlias("TransIDH")
    private String TransIDH;
    @XStreamAlias("ProcessTime")
    private String ProcessTime;
    @XStreamAlias("Response")
    private Response response;
    @XStreamAlias("SvcCont")
    private String svcCont;

    public String getOrigDomain() {
        return OrigDomain;
    }

    public void setOrigDomain(String origDomain) {
        OrigDomain = origDomain;
    }

    public String getHomeDomain() {
        return HomeDomain;
    }

    public void setHomeDomain(String homeDomain) {
        HomeDomain = homeDomain;
    }

    public String getBIPCode() {
        return BIPCode;
    }

    public void setBIPCode(String BIPCode) {
        this.BIPCode = BIPCode;
    }

    public String getActivityCode() {
        return ActivityCode;
    }

    public void setActivityCode(String activityCode) {
        ActivityCode = activityCode;
    }

    public String getActionCode() {
        return ActionCode;
    }

    public void setActionCode(String actionCode) {
        ActionCode = actionCode;
    }

    public String getProcID() {
        return ProcID;
    }

    public void setProcID(String procID) {
        ProcID = procID;
    }

    public String getTransIDO() {
        return TransIDO;
    }

    public void setTransIDO(String transIDO) {
        TransIDO = transIDO;
    }

    public String getTransIDH() {
        return TransIDH;
    }

    public void setTransIDH(String transIDH) {
        TransIDH = transIDH;
    }

    public String getProcessTime() {
        return ProcessTime;
    }

    public void setProcessTime(String processTime) {
        ProcessTime = processTime;
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
