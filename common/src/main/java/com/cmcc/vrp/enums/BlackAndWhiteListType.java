package com.cmcc.vrp.enums;

/**
 * 活动类型
 */
public enum BlackAndWhiteListType {

    NOLIST(0, "无黑白名单"),
    WHITELIST(1, "白名单"),
    BLACKLIST(2, "黑名单");

    private Integer code;

    private String name;

    BlackAndWhiteListType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * @param value
     * @return
     */
    public static BlackAndWhiteListType fromValue(int value) {
        for (BlackAndWhiteListType type : BlackAndWhiteListType.values()) {
            if (type.getCode().equals(value)) {
                return type;
            }
        }

        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return
     */
    public String getname() {
        return name;
    }

    /**
     * @param name
     */
    public void setname(String name) {
        this.name = name;
    }
}
