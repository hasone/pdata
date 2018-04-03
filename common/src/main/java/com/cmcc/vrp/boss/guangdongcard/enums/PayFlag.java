package com.cmcc.vrp.boss.guangdongcard.enums;

/**
 * 支付方式
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
public enum PayFlag {
    ENTERPRISE(0, "集团统付"),
    PERSONAL(1, "个人支付");

    private int value;
    private String message;

    PayFlag(int value, String message) {
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
