package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/18.
 */
@XStreamAlias("AdditionResult")
public class AdditionResult {

    @XStreamAlias("OperSeq")
    private String operSeq;

    @XStreamAlias("SuccNum")
    private String succNum;

    @XStreamAlias("SuccInfo")
    private SuccInfo succInfo;

    @XStreamAlias("FailNum")
    private String failNum;

    @XStreamAlias("FailInfo")
    private FailInfo failInfo;

    @XStreamAlias("TransIDO")
    private String transIDO;
    
    @XStreamAlias("Status")
    private String status;

    public String getOperSeq() {
        return operSeq;
    }

    public void setOperSeq(String operSeq) {
        this.operSeq = operSeq;
    }

    public String getSuccNum() {
        return succNum;
    }

    public void setSuccNum(String succNum) {
        this.succNum = succNum;
    }

    public SuccInfo getSuccInfo() {
        return succInfo;
    }

    public void setSuccInfo(SuccInfo succInfo) {
        this.succInfo = succInfo;
    }

    public String getFailNum() {
        return failNum;
    }

    public void setFailNum(String failNum) {
        this.failNum = failNum;
    }

    public FailInfo getFailInfo() {
        return failInfo;
    }

    public void setFailInfo(FailInfo failInfo) {
        this.failInfo = failInfo;
    }

    public String getTransIDO() {
        return transIDO;
    }

    public void setTransIDO(String transIDO) {
        this.transIDO = transIDO;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
