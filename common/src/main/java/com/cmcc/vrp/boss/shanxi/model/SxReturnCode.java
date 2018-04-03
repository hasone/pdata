package com.cmcc.vrp.boss.shanxi.model;

/**
 * Created by leelyn on 2016/8/28.
 */
public enum SxReturnCode {

    SUCCESS("0", "充值成功"),
    FAILD("0001", "充值失败"),
    RESP_ILLEGALITY("0002", "Boss侧响应异常"),
    PARA_ILLEGALITY("2001", "参数非法"),
	CODE_PARAM_JSON_ERROR("12", "json解析错误");

    private String code;
    private String msg;

    SxReturnCode(String code, String msg) {
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
