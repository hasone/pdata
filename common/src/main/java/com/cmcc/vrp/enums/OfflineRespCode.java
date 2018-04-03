package com.cmcc.vrp.enums;

public enum OfflineRespCode {
    SUCCESS(0, "成功"),
    FAILED(1, "失败");

    private int value;
    private String message;

    OfflineRespCode(Integer value, String message) {
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