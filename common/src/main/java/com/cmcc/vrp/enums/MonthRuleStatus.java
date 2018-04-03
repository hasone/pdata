package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

public enum MonthRuleStatus {

    WAIT(0, "待赠送"),
    PROCESSING(1, "进行中"),
    COMPLETE(2, "已完成"),
    DELETED(3, "已取消");

	/*Map<Integer, String> map = new HashMap<Integer, String>() {
		{
			
		put(WAIT.getCode(), WAIT.getMessage());
	}};*/

    private Integer code;

    private String message;

    MonthRuleStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (MonthRuleStatus item : MonthRuleStatus.values()) {
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
