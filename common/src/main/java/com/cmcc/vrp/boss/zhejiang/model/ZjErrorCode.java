package com.cmcc.vrp.boss.zhejiang.model;

/**
 * Created by leelyn on 2016/8/2.
 */
public enum ZjErrorCode {

    SUCCESS("0000000", "充值成功"),
    FAILD("010003", "充值失败"),
    PARA_ERROR("111111", "参数错误");

    private String code;
    private String msg;

    ZjErrorCode(String code, String msg) {
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
