package com.cmcc.vrp.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 运营商类型
 * <p>
 * Created by sunyiwei on 2016/6/13.
 */
public enum IspType {
    CMCC("M", "中国移动"),
    UNICOM("U", "中国联通"),
    TELECOM("T", "中国电信"),
    ALL("A", "三网通"),
    DEFAULT("","");

    private String value;
    private String message;

    IspType(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
    * @param value
    * @return
    */
    public static IspType fromValue(String value) {
        if (StringUtils.isBlank(value)) {
            return DEFAULT;
        }
        for (IspType tpye : IspType.values()) {
            if (tpye.getValue().equalsIgnoreCase(value)) {
                return tpye;
            }
        }
        return DEFAULT;
    }
}
