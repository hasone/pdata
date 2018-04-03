package com.cmcc.vrp.ec.bean.gd;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("EntSrvRegResp")
public class EntSrvRegResp {

    @XStreamAlias("Result")
    private String result;
    
    @XStreamAlias("ResultMsg")
    private String resultMsg;

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
}
