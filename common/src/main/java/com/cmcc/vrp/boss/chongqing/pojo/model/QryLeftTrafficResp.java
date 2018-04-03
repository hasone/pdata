package com.cmcc.vrp.boss.chongqing.pojo.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author lgk8023
 *
 */
public class QryLeftTrafficResp {

    @SerializedName("res_code")
    private String resCode;
    
    @SerializedName("res_desc")
    private String resDesc;
    
    @SerializedName("result")
    private QryLeftTrafficResult result;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResDesc() {
        return resDesc;
    }

    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }

    public QryLeftTrafficResult getResult() {
        return result;
    }

    public void setResult(QryLeftTrafficResult result) {
        this.result = result;
    }
}
