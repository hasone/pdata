package com.cmcc.vrp.boss.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * core平台（包括unionflow和fdn）充值反馈接口
 * <p>
 * Created by sunyiwei on 2015/11/28.
 */
@XStreamAlias("Request")
public class CoreCallbackReq {
    //boss侧响应流水号
    @XStreamAlias("OperSeq")
    private String operSeq;

    @XStreamAlias("SuccInfoList")
    private List<SuccInfo> succInfo;

    @XStreamAlias("FailInfoList")
    private List<FailInfo> failInfo;

    public String getOperSeq() {
        return operSeq;
    }

    public void setOperSeq(String operSeq) {
        this.operSeq = operSeq;
    }

    public List<SuccInfo> getSuccInfo() {
        return succInfo;
    }

    public void setSuccInfo(List<SuccInfo> succInfo) {
        this.succInfo = succInfo;
    }

    public List<FailInfo> getFailInfo() {
        return failInfo;
    }

    public void setFailInfo(List<FailInfo> failInfo) {
        this.failInfo = failInfo;
    }
}
