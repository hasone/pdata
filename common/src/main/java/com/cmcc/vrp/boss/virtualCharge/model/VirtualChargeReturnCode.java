package com.cmcc.vrp.boss.virtualCharge.model;

/**
 * Created by qinqinyan on 2017/5/9.
 */
public enum VirtualChargeReturnCode {

    SUCCESS("0000", "充值成功"),
    FAILD("0001", "充值失败"),
    PARA_ILLEGALITY("0002", "参数错误"),
    RESP_ILLEGALITY("0003", "返回包体为空");

    private String code;
    private String msg;

    VirtualChargeReturnCode(String code, String msg) {
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
