package com.cmcc.vrp.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName: NoticeMsgStatus
 * @Description: TODO
 * @author: Rowe
 * @date: 2016年5月23日 下午12:36:11
 */
public enum NoticeMsgStatus {
    NO(0, "未下发通知短信"),
    YES(1, "已下发通知短信");


    private Integer code;

    private String message;

    NoticeMsgStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (NoticeMsgStatus item : NoticeMsgStatus.values()) {
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
