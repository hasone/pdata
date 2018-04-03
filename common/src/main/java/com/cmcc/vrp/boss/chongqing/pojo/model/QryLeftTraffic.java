package com.cmcc.vrp.boss.chongqing.pojo.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author lgk8023
 *
 */
public class QryLeftTraffic {

    @SerializedName("TELNUM")
    private String telnum;
    @SerializedName("CYCLE")
    private String cycle;
    @SerializedName("TYPE")
    private String type;
    public String getTelnum() {
        return telnum;
    }
    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }
    public String getCycle() {
        return cycle;
    }
    public void setCycle(String cycle) {
        this.cycle = cycle;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
