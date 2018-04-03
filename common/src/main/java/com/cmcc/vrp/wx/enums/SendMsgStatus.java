package com.cmcc.vrp.wx.enums;

/**
 * Created by leelyn on 2017/1/9.
 */
public enum SendMsgStatus {

    OK("00000", "推送成功"),
    FAIL("00001", "推送失败");

    private String code;

    private String msg;

    SendMsgStatus(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
