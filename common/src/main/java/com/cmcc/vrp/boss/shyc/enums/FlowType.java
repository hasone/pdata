package com.cmcc.vrp.boss.shyc.enums;

/**
 * 上海月呈定义的流量类型
 *
 * Created by sunyiwei on 2017/3/14.
 */
public enum FlowType {
    NATIONAL(0, "全国流量"),
    PROVINCE(1, "省内流量");

    private int code;
    private String message;

    FlowType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
