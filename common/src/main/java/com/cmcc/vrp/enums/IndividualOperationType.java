package com.cmcc.vrp.enums;

public enum IndividualOperationType {
    COMMON_REDPACKET(1, "个人普通红包"),
    COMMON_REDPACKET_BACK(2, "个人普通红包退回"),
    LUCKY_REDPACKET(3, "拼手气红包"),
    LUCKY_REDPACKET_BACK(4, "拼手气红包退回"),
    PRESNET(5, "赠送"),
    PRESNET_BACK(6, "赠送"),
    FLOWCOIN_EXCHANGE(7, "个人流量币兑换"),
    FLOWCOIN_EXCHANGE_BACK(8, "个人流量币兑换退回"),
    FLOWCOIN_PURCHASE(9, "个人流量币购买"),
    FLOWCOIN_PURCHASE_BACK(10, "个人流量币购买");

    private Integer code;

    private String name;

    IndividualOperationType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * @param value
     * @return
     */
    public static ActivityType fromValue(int value) {
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
