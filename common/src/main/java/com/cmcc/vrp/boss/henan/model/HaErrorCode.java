package com.cmcc.vrp.boss.henan.model;

/**
 * Created by leelyn on 2016/6/26.
 */
public enum HaErrorCode {

    send("00000", "BOSS已接受请求"),
    faild("00001", "失败"),
    parameter_error("00002", "参数错误");

    private String code;
    private String msg;

    HaErrorCode(String code, String msg) {
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
