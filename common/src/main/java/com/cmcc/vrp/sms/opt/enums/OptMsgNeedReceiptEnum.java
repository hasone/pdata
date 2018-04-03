package com.cmcc.vrp.sms.opt.enums;

/**
 * 开放平台短信通道是否需要回执的枚举类
 *
 * Created by sunyiwei on 2017/2/13.
 */
public enum OptMsgNeedReceiptEnum {
    NOT_NEED(0, "不需要回执"),
    NEED(1, "需要回执");

    private int value;
    private String message;

    OptMsgNeedReceiptEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getValue() {
        return value;
    }
}
