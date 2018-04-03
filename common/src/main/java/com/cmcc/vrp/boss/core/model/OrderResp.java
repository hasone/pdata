package com.cmcc.vrp.boss.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/8/17.
 */
@XStreamAlias("Response")
public class OrderResp {
    @XStreamAlias("OperSeq")
    private String operSeq;

    @XStreamAlias("TransID")
    private String transID;

    @XStreamAlias("Status")
    private String status;

    @XStreamAlias("ErrDesc")
    private String errDesc;

    public String getOperSeq() {
        return operSeq;
    }

    public void setOperSeq(String operSeq) {
        this.operSeq = operSeq;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }
}
