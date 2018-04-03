package com.cmcc.vrp.boss.gd.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("QryECSyncInfoResp")
public class QryECSyncInfoResp {

    @XStreamAlias("Result")
    private String result;
    
    @XStreamAlias("ResultMsg")
    private String resultMsg;
    
    @XStreamAlias("ECSyncInfo")
    private ECSyncInfo ecSyncInfo;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public ECSyncInfo getEcSyncInfo() {
        return ecSyncInfo;
    }

    public void setEcSyncInfo(ECSyncInfo ecSyncInfo) {
        this.ecSyncInfo = ecSyncInfo;
    }
    
}
