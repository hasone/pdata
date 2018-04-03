package com.cmcc.vrp.boss.fujian.model;

/**
 * Created by leelyn on 2016/7/27.
 */
public enum FjReturnCode {

    SUCCESS("0000", "充值成功"),
    FAILD("5119", "充值失败"),
    PARA_ILLEGALITY("11", "参数缺失"),
    FLOW_CHARGEING("2001", "流量充值处理中"),
    AUTH_FAILD("5001", "鉴权失败 ");


    private String code;
    private String msg;

    FjReturnCode(String code, String msg) {
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
