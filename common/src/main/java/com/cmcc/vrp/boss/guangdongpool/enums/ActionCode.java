package com.cmcc.vrp.boss.guangdongpool.enums;

/**
 * 动作码
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
public enum ActionCode {
    REQUEST(1, "请求"),
    RESPONSE(2, "应答");

    private int value;
    private String message;

    ActionCode(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
