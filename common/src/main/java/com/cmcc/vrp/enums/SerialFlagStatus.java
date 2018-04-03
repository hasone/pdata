package com.cmcc.vrp.enums;

/**
 * 连续签到状态
 * @author qinqinyan
 */
public enum SerialFlagStatus {
    SERIAL(0, "连续"),
    NOT_SERIAL(1, "不连续");

    private Integer value;
    private String message;

    SerialFlagStatus(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
