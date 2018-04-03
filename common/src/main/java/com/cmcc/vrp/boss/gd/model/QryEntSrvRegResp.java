package com.cmcc.vrp.boss.gd.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("QryEntSrvRegResp")
public class QryEntSrvRegResp {

    @XStreamAlias("Result")
    private String result;
    
    @XStreamAlias("ResultMsg")
    private String resultMsg;
    
    @XStreamAlias("EntSrvReg")
    private EntSrvReg entSrvReg;

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

    public EntSrvReg getEntSrvReg() {
        return entSrvReg;
    }

    public void setEntSrvReg(EntSrvReg entSrvReg) {
        this.entSrvReg = entSrvReg;
    }
}
