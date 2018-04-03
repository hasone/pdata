package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ChargeLogType
 * @Description: 充值日志类型
 * @author: Rowe
 * @date: 2016年1月22日 下午5:12:38
 */
public enum ChargeLogType {
    CI(1, "充值接口充值日志"),
    RG(2, "红包赠送充值日志"),
    MG(3, "包月赠送充值日志"),
    CG(4, "普通赠送充值日志"),
    FT(5, "流量券充值日志"),
    FC(6, "营销卡充值日志");

    private Integer code;

    private String message;

    ChargeLogType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (ChargeLogType item : ChargeLogType.values()) {
            map.put(String.valueOf(item.getCode()), item.getMessage());
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
