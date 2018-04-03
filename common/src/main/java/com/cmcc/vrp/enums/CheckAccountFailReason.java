package com.cmcc.vrp.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum CheckAccountFailReason {

    NOT_FIND_FILE(1, "找不到对账文件"),
    READ_TXT_FAIL(2, "解析txt文件失败"),
    RESPONSE_TIME_ERROR(3, "返回时间格式不对，无法解析"),
    NO_DATA_IN_DB(4, "数据库中无该时段账目");

    private Integer code;

    private String message;

    CheckAccountFailReason(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (CheckAccountFailReason item : CheckAccountFailReason.values()) {
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
