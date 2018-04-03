package com.cmcc.vrp.boss.jsof.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author lgk8023
 *
 */
public class JsofCallback {
    @SerializedName("ret_code")
    private String retCode;
    
    @SerializedName("sporder_id")
    private String sporderId;
    
    @SerializedName("ordersuccesstime")
    private String ordersuccesstime;
    
    @SerializedName("err_msg")
    private String errMsg;

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getSporderId() {
        return sporderId;
    }

    public void setSporderId(String sporderId) {
        this.sporderId = sporderId;
    }

    public String getOrdersuccesstime() {
        return ordersuccesstime;
    }

    public void setOrdersuccesstime(String ordersuccesstime) {
        this.ordersuccesstime = ordersuccesstime;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "JsofCallback [retCode=" + retCode + ", sporderId=" + sporderId + ", ordersuccesstime=" + ordersuccesstime + ", errMsg=" + errMsg + "]";
    }
    
}
