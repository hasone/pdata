package com.cmcc.vrp.enums;

/**
 * Created by sunyiwei on 2016/8/23.
 */
public enum QRCodeExtendParam {
    ACTIVITY_RECORD_ID("activityRecordId", "活动记录ID"),
    QRCODE_SIZE_BASIC("43", "二维码基础大小");

    private String key;
    private String message;

    QRCodeExtendParam(String key, String message) {
        this.key = key;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
