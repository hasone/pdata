package com.cmcc.vrp.boss.shangdong.model;

/**
 * Created by leelyn on 2016/6/28.
 */
public enum SDReturnCode {

    SUCCESS("100", "充值成功"),
    FAILD("101", "充值失败"),
    PARA_ILLEGALITY("11", "参数缺失");

    private String code;
    private String msg;

    SDReturnCode(String code, String msg) {
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
