package com.cmcc.vrp.boss.chongqing.pojo.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author lgk8023
 *
 */
public class NewChargeReq {

    @SerializedName("TELNUM")
    private String telnum;
    
    @SerializedName("NCODE")
    private String ncode;
    
    @SerializedName("EFFTYPE")
    private String effType;
    
    @SerializedName("PARM")
    private String parm;
    
    @SerializedName("OPERTYPE")
    private String operType;

    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }

    public String getNcode() {
        return ncode;
    }

    public void setNcode(String ncode) {
        this.ncode = ncode;
    }

    public String getEffType() {
        return effType;
    }

    public void setEffType(String effType) {
        this.effType = effType;
    }

    public String getParm() {
        return parm;
    }

    public void setParm(String parm) {
        this.parm = parm;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }
}
