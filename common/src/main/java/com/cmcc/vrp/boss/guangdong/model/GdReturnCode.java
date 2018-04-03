package com.cmcc.vrp.boss.guangdong.model;

/**
 * Created by leelyn on 2016/7/8.
 */
public enum GdReturnCode {

    SUCCESS("0000", "充值成功"),
    FAILD("0001", "充值失败"),
    ERR_940037("940037", "重复添加集团用户的成员"),
    ERR_983304("983304", "重复添加集团用户的成员"),
    ERR_940003("940003", "重复添加集团用户的成员"),
    PARA_ILLEGALITY("0002", "参数错误"),
    RESP_ILLEGALITY("0003", "返回包体为空");

    private String code;
    private String msg;

    GdReturnCode(String code, String msg) {
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
