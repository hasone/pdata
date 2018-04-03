package com.cmcc.vrp.boss.guangxi.model;

/**
 * Created by leelyn on 2016/9/13.
 */
public enum GxReturnCode {

    SUCCESS("00", "充值成功"),
    FAILD("01", "充值失败");

    private String code;
    private String msg;

    GxReturnCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
