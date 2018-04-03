package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

public enum LotteryActivityProbablityType {

    MANUAL_SET(1, "活动中的奖项概率手动设置固定值"),
    DYNAMIC_SET(0, "活动中的奖项概率自动调整");
    private Integer code;

    private String message;

    LotteryActivityProbablityType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (LotteryActivityProbablityType item : LotteryActivityProbablityType.values()) {
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
