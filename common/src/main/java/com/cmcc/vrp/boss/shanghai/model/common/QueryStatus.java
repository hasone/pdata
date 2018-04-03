package com.cmcc.vrp.boss.shanghai.model.common;

/**
 * Created by leelyn on 2016/8/11.
 */
public enum QueryStatus {

    NO_DEAL("0", "初始化"),
    HAS_SUBMIT("1", "在途单"),
    SUCCESS_DEAL("2", "正常归档"),
    FAILD_DEAL("3", "订单异常");

    private String code;
    private String msg;

    QueryStatus(String code, String msg) {
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
