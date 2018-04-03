package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/18.
 */
@XStreamAlias("OperSeqList")
public class OperSeqList {

    @XStreamAlias("OperSeq")
    private String operSeq;

    public String getOperSeq() {
        return operSeq;
    }

    public void setOperSeq(String operSeq) {
        this.operSeq = operSeq;
    }
}
