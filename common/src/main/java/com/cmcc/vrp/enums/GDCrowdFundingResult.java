package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

public enum GDCrowdFundingResult {

    Crowd_Funding(0, "众筹中"),
    Crowd_Funding_Success(1, "众筹成功"),
    Crowd_Funding_Fail(2, "众筹结束");

    private Integer code;

    private String message;

    GDCrowdFundingResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (GDCrowdFundingResult item : GDCrowdFundingResult.values()) {
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
