package com.cmcc.vrp.boss.shanghai.model;

/**
 * Created by leelyn on 2016/7/12.
 */
public enum ShReturnCode {

    SUCCESS("0000", "充值成功"),
    FAILD("0001", "充值失败"),
    RESP_ILLEGALITY("0002", "Boss侧响应异常"),
    PARA_ILLEGALITY("2001", "参数非法"),
    STOP_COUNT("2003","达到暂停值");

    private String code;
    private String msg;

    ShReturnCode(String code, String msg) {
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
