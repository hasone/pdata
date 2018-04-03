package com.cmcc.vrp.boss.shyc.enums;

/**
 * 上海月呈定义的回调类型
 *
 * Created by sunyiwei on 2017/3/14.
 */
public enum CallbackType {
    FLOW(1, "流量"),
    FEE(2, "话费"),
    VOICE(3, "语音");

    private int type;
    private String message;

    CallbackType(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
