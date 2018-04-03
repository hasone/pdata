package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信用户类型
 * @author qinqinyan
 */
public enum AuthStatus {
    ERROR(-1, "错误"),
    NO_FOCUS(0, "未关注"),
    FOCUS_BUT_NOT_BINDED(1, "已关注,未绑定手机号"),
    FOCUS_AND_BINDED(2, "已关注,且绑定手机号");

    private Integer code;

    private String message;

    AuthStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (AuthStatus item : AuthStatus.values()) {
            map.put(item.getCode().toString(), item.getMessage());
        }
        return map;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
