package com.cmcc.vrp.boss.hunan.model;

/**
 * Created by sunyiwei on 2016/9/19.
 */
public enum HNOperation {
    ORDER("0", "订购"),
    CANCEL("1", "取消"),
    MODIFY("2", "变更");

    private String code;
    private String message;

    HNOperation(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
