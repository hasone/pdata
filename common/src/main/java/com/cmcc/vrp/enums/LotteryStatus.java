package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

public enum LotteryStatus {

    Not_create(0, "未生成活动"),
    Before_Start(1, "活动未开始"),
    Processing(2, "活动进行中"),
    End(3, "活动结束"),
    Delete(4, "已删除");

    private Integer code;

    private String message;

    LotteryStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (LotteryStatus item : LotteryStatus.values()) {
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
