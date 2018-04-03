package com.cmcc.vrp.boss.guangdongpool.enums;

/**
 * 产品操作类型
 *
 * Created by sunyiwei on 2016/11/18.
 */
public enum ProductOptType {
    ORDER(0,"订购"),
    MODIFY(2, "修改属性"),
    CANCEL(4, "注销");

    private int value;
    private String message;

    ProductOptType(int value, String message) {
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
