package com.cmcc.vrp.boss.guangdongpool.enums;

/**
 * 同步异步方式
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
public enum DealKind {
    SYNC(0, "同步操作"),
    ASYNC(1, "异步操作");

    private int value;
    private String message;

    DealKind(int value, String message) {
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
