package com.cmcc.vrp.boss.guangxi.enums;

/**
 * Created by leelyn on 2016/9/23.
 */
public enum CallbackResult {

    SUCCESS("0000", "Success");

    private String rspCode;

    private String rspDsc;

    CallbackResult(String rspCode, String rspDsc) {
        this.rspCode = rspCode;
        this.rspDsc = rspDsc;
    }

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspDsc() {
        return rspDsc;
    }

    public void setRspDsc(String rspDsc) {
        this.rspDsc = rspDsc;
    }
}
