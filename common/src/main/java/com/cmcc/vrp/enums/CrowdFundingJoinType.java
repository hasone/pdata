package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

public enum CrowdFundingJoinType {
    
    Big_Enterprise(1, "大规模企业"),
    Small_Enterprise(2, "中小规模企业");

    private Integer code;

    private String message;

    CrowdFundingJoinType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (CrowdFundingJoinType item : CrowdFundingJoinType.values()) {
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
