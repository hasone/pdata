package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/22.
 */
@XStreamAlias("AdditionRsp")
public class AdditionRsp {

    @XStreamAlias("Status")
    private String status;

    @XStreamAlias("OperSeqList")
    private OperSeqList operSeqList;

    @XStreamAlias("ErrDesc")
    private String errDesc;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OperSeqList getOperSeqList() {
        return operSeqList;
    }

    public void setOperSeqList(OperSeqList operSeqList) {
        this.operSeqList = operSeqList;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }
}
