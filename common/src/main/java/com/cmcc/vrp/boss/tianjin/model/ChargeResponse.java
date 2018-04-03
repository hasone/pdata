package com.cmcc.vrp.boss.tianjin.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月17日 上午9:01:12
*/
public class ChargeResponse {
    private String respCode;
    
    private String respDesc;
    
    private Result result;
    
    public String getRespCode() {
        return respCode;
    }


    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }


    public String getRespDesc() {
        return respDesc;
    }


    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }


    public Result getResult() {
        return result;
    }


    public void setResult(Result result) {
        this.result = result;
    }


    @Override
    public String toString() {
        return "ChargeResponse [respCode=" + respCode + ", respDesc=" + respDesc + ", result=" + result + "]";
    }

}

