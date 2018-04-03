package com.cmcc.vrp.boss.guangdongpool.enums;

/**
 * 生效方式
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
public enum EffType {
    RIGHT_NOW(2, "立即"),
    NEXT_MONTH(3, "下月"),
    DEFAULT(0, "默认");

    private int value;
    private String message;

    EffType(int value, String message) {
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
