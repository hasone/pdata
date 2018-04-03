package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

public enum SchedulerGroup {

    DEFAULT("default", "定时任务调度器默认组");

    private String code;

    private String message;

    SchedulerGroup(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (SchedulerGroup item : SchedulerGroup.values()) {
            map.put(item.getCode(), item.getMessage());
        }
        return map;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}