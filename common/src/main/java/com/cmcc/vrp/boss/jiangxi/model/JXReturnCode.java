package com.cmcc.vrp.boss.jiangxi.model;

/**
 * Created by leelyn on 2016/7/7.
 */
public enum JXReturnCode {

    SUCCESS("00", "充值成功"),
    SYSTEM_EXCP("-1", "系统异常，处理失败"),
    AUTH_FAILD("02", "IP鉴权不通过"),
    DATA_ILLEGALITY("03", "数据非法加密"),
    PARA_ILLEGALITY("04", "ECCODE参数非法"),
    FAILD("05", "原因不明");

    private String code;
    private String msg;

    JXReturnCode(String code, String msg) {
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
