package com.cmcc.vrp.boss.hebei.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/8/19.
 */
@XStreamAlias("InterBOSS")
public class InterBOSSReq {

    @XStreamAlias("OrigDomain")
    private String origDomain;

    @XStreamAlias("HomeDomain")
    private String homeDomain;

    @XStreamAlias("BIPCode")
    private String bipCode;

    @XStreamAlias("ActivityCode")
    private String activtyCode;

    @XStreamAlias("ActionCode")
    private String actionCode;

    @XStreamAlias("ProcID")
    private String procID;

    @XStreamAlias("TransIDO")
    private String transIDO;

    @XStreamAlias("ProcessTime")
    private String processTime;

    @XStreamAlias("SvcCont")
    private String svcCont;

    public String getOrigDomain() {
        return origDomain;
    }

    public void setOrigDomain(String origDomain) {
        this.origDomain = origDomain;
    }

    public String getHomeDomain() {
        return homeDomain;
    }

    public void setHomeDomain(String homeDomain) {
        this.homeDomain = homeDomain;
    }

    public String getBipCode() {
        return bipCode;
    }

    public void setBipCode(String bipCode) {
        this.bipCode = bipCode;
    }

    public String getActivtyCode() {
        return activtyCode;
    }

    public void setActivtyCode(String activtyCode) {
        this.activtyCode = activtyCode;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getProcID() {
        return procID;
    }

    public void setProcID(String procID) {
        this.procID = procID;
    }

    public String getTransIDO() {
        return transIDO;
    }

    public void setTransIDO(String transIDO) {
        this.transIDO = transIDO;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public String getSvcCont() {
        return svcCont;
    }

    public void setSvcCont(String svcCont) {
        this.svcCont = svcCont;
    }
}
