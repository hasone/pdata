package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName: SdAccountRecordStatus 
 * @Description: 山东预付账户操作结果状态
 * @author: Rowe
 * @date: 2017年9月4日 上午10:23:24
 */
public enum SdAccountRecordStatus {
    PROCESSING(0, "同步中"),
    SUCCESS(1, "同步成功"),
    FAILURE(2, "同步失败");

    private Integer code;

    private String message;

    SdAccountRecordStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (SdAccountRecordStatus item : SdAccountRecordStatus.values()) {
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
