package com.cmcc.vrp.boss.core;

/**
 * core渠道的响应码,自定义
 * <p>
 * Created by leelyn on 2016/8/17.
 * edit by sunyiwei
 */
public enum CoreResultCodeEnum {

    SUCCESS("0", "充值成功"),
    FAILD("1", "充值失败"),
    PARA_ILLEGALITY("11", "参数缺失");

    private String code;
    private String msg;

    CoreResultCodeEnum(String code, String msg) {
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
