package com.cmcc.vrp.boss.beijing.model;

/**
 * Created by leelyn on 2016/8/11.
 */
public enum QueryStatus {

    NO_DEAL("0", "未受理"),
    HAS_SUBMIT("1", "已提交"),
    SUCCESS_DEAL("2", "办理成功"),
    FAILD_DEAL("3", "办理失败");

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
