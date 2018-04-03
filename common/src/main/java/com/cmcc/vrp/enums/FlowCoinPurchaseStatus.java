package com.cmcc.vrp.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wujiamin
 * @date 2016年9月23日下午2:09:01
 */
public enum FlowCoinPurchaseStatus {
    WAITED(0, "待支付"),
    PROCESSING(1, "支付中"),
    SUCCESS(2, "支付成功"),
    FAIL(3, "支付失败"),
    CANCEL(4, "取消支付");

    private Integer code;

    private String message;

    FlowCoinPurchaseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (FlowCoinPurchaseStatus item : FlowCoinPurchaseStatus.values()) {
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
