package com.cmcc.vrp.boss.guangdongcard.enums;

/**
 * 用户类型
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
public enum CustType {
    PERSONAL(1, "个人"),
    ENTERPRISE(3, "企业");

    private int value;
    private String message;

    CustType(int value, String message) {
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
