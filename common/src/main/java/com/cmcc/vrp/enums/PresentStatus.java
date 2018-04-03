package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName: PresentStatus 
 * @Description: 包月赠送活动状态
 * @author: Rowe
 * @date: 2017年7月7日 上午9:09:07
 */
public enum PresentStatus {
    
    PROCESSING(0,"进行中"),
    COMPLETED(1,"已完成");

    private Integer code;

    private String message;

    PresentStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (PresentStatus item : PresentStatus.values()) {
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
