package com.cmcc.vrp.enums;

/**
 * Created by qinqinyan on 2016/10/28.
 * 活动类型，与营销模板服务对应
 */
public enum ActivityTemplateType {

    REDPACKET(0, "红包"),
    LOTTERY(1, "转盘"),
    GOLDENBALL(2, "砸金蛋"),
    COMMON_REDPACKET(3, "个人普通红包"),
    LUCKY_REDPACKET(4, "拼手气红包");

    private Integer code;

    private String name;

    ActivityTemplateType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * @param value
     * @return
     */
    public static ActivityType fromValue(Integer value) {
        if (value == null) {
            return null;
        }

        for (ActivityType type : ActivityType.values()) {
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
