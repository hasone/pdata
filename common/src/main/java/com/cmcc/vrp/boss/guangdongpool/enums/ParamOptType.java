package com.cmcc.vrp.boss.guangdongpool.enums;

/**
 * 操作类型
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
public enum ParamOptType {
    ORDER(0, "订购"),
    MODIFY(2, "修改"),
    CANCEL(4, "取消");

    private int value;
    private String message;

    ParamOptType(int value, String message) {
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
