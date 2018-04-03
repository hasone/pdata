package com.cmcc.vrp.boss.guangdongpool.enums;

/**
 * 业务类型
 *
 * Created by sunyiwei on 2016/11/18.
 */
public enum MemberOptType {
    ORDER(0, "开通"),
    PAUSE(1, "暂停"),
    CANCEL(2, "注销"),
    RESUME(3, "恢复"),
    MODIFY_PROPS(4, "属性变更(成员业务开通属性、产品属性)"),
    MODIFY_BUSI(5, "套餐变更");

    private int value;
    private String message;

    MemberOptType(int value, String message) {
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
