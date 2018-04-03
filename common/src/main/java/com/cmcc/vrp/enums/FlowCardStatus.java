/**
 * @Title: FlowCardStatus.java
 * @Package com.cmcc.vrp.enums
 * @author: qihang
 * @date: 2015年5月19日 上午10:41:55
 * @version V1.0
 */
package com.cmcc.vrp.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName: FlowCardStatus
 * @Description: TODO
 * @author: qihang
 * @date: 2015年5月19日 上午10:41:55
 *
 */
public enum FlowCardStatus {

    WAIT(0, "未使用"),
    PROCESSING(1, "正在充值"),
    CHARGE_SUCCESS(2, "已使用"),
    CHARGE_FAIL(3, "充值失败"),
    EXPIRED(4, "已过期");

    private Integer code;

    private String message;

    FlowCardStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (FlowCardStatus item : FlowCardStatus.values()) {
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
