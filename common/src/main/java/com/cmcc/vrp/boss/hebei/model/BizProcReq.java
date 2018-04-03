package com.cmcc.vrp.boss.hebei.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/8/19.
 */
@XStreamAlias("BizProcReq")
public class BizProcReq {

    @XStreamAlias("GROUPNO")
    private String groupNo;

    @XStreamAlias("Grpsubsid")
    private String grpsubSid;

    @XStreamAlias("MSISDN")
    private String msisdn;

    @XStreamAlias("FLUXAMT")
    private String fluxamt;

    @XStreamAlias("SENDSMS")
    private String sendsms;

    @XStreamAlias("ASYNORDER")
    private String ASYNORDER;

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getGrpsubSid() {
        return grpsubSid;
    }

    public void setGrpsubSid(String grpsubSid) {
        this.grpsubSid = grpsubSid;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getFluxamt() {
        return fluxamt;
    }

    public void setFluxamt(String fluxamt) {
        this.fluxamt = fluxamt;
    }

    public String getSendsms() {
        return sendsms;
    }

    public void setSendsms(String sendsms) {
        this.sendsms = sendsms;
    }

    public String getASYNORDER() {
        return ASYNORDER;
    }

    public void setASYNORDER(String ASYNORDER) {
        this.ASYNORDER = ASYNORDER;
    }
}
