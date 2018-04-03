package com.cmcc.vrp.boss.sichuan.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午11:04:08
*/
public class SCOpenResponse {

    private String resCode;

    private String resMsg;

    private SCOpenResponseOutData outData;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public SCOpenResponseOutData getOutData() {
        return outData;
    }

    public void setOutData(SCOpenResponseOutData outData) {
        this.outData = outData;
    }

}
