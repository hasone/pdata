package com.cmcc.vrp.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum UnitCode {

    UNIT_KB(0, "KB"),
    UNIT_MB(1, "MB"),
    UNIT_GB(2, "GB"),
    UNIT_TB(3, "TB");

    private Integer code;

    private String message;

    UnitCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, Integer> toMap() {
        Map<String, Integer> map = new LinkedHashMap<String, Integer>();
        for (UnitCode item : UnitCode.values()) {
            map.put(item.getMessage(), item.getCode());
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
