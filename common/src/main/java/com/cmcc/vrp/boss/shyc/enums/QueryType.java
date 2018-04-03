package com.cmcc.vrp.boss.shyc.enums;

/**
 * 查询流量包定义的流量包类型
 *
 * Created by sunyiwei on 2017/3/14.
 */
public enum QueryType {
    ALL(0, "三网"),
    MOBILE(1, "移动"),
    UNICOM(2, "联通"),
    TELECOM(3, "电信");

    private int type;
    private String message;

    QueryType(int type, String message) {
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
