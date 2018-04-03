package com.cmcc.vrp.boss.beijing.model;

/**
 * Created by leelyn on 2016/7/19.
 */
public enum BjReturnCode {

    SUCCESS("0", "充值成功"),
    FAILD("1", "充值失败"),
    PARA_ILLEGALITY("11", "参数缺失");

    private String code;
    private String msg;

    BjReturnCode(String code, String msg) {
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
