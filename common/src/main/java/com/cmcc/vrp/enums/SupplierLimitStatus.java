package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 供应商限额，供应商产品限额启用标志位
 * @author qinqinyan
 * @date 
 */
public enum SupplierLimitStatus {

    OFF(0, "关闭限额"),
    ON(1, "开启限额");

    private Integer code;

    private String message;

    SupplierLimitStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (SupplierLimitStatus item : SupplierLimitStatus.values()) {
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
