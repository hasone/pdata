package com.cmcc.vrp.wx.enums;

/**
 * Created by leelyn on 2016/8/15.
 */
public enum BossQueryResult {

    SUCCESS("100", "处理成功"),
    FAILD("101", "处理失败"),
    PROCESSING("102", "正在处理中"),
    UNACCEPT("103", "未受理"),
    EXCEPTION("104", "查询异常");

    private String code;
    private String msg;

    BossQueryResult(String code, String msg) {
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
