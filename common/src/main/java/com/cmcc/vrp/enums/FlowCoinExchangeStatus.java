package com.cmcc.vrp.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wujiamin
 * @date 2016年9月23日下午2:09:01
 */
public enum FlowCoinExchangeStatus {
    PROCESSING(0, "兑换中"),
    SUCCESS(1, "兑换成功"),//兑换成功，表示流量充值成功
    FAIL_BACK_PROC(2, "兑换失败-退回流量币中"),//流量充值失败，正在退回流量币
    FAIL_BACK_SUCC(3, "兑换失败-退回流量币成功"),//流量充值失败，退回流量币成功
    FAIL_BACK_FAIL(4, "兑换失败-退回流量币失败");//流量充值失败，退回流量币失败

    private Integer code;

    private String message;

    FlowCoinExchangeStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (FlowCoinExchangeStatus item : FlowCoinExchangeStatus.values()) {
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
