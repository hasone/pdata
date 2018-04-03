package com.cmcc.vrp.boss.guangdongpool.enums;

/**
 * 响应编码
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
public enum ReturnInfo {
    SUCCESS(0, "成功"),
    NOT_GD_CMCC(1, "成员号码不是广东移动号码"),
    NOT_ORDER(2, "集团未订购此产品"),
    INVALID_PARAM(3, "无效的充值参数"), //平台自定义
    UNDEFINED(99, "未定义");

    private int value;
    private String message;

    ReturnInfo(int value, String message) {
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

