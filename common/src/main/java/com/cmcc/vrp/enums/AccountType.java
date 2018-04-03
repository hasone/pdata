package com.cmcc.vrp.enums;

/**
 * Created by sunyiwei on 16-3-29.
 */
public enum AccountType {
    ENTERPRISE(-1, "非活动"),
    REDPACKET(0, "红包帐户"),
    LOTTERY(1, "大转盘帐户"),
    FLOWCARD(2, "流量卡帐户"),
    MDRCCARD(3, "营销卡帐户"),
    GOLDENBALL(4, "砸金蛋账户");

    private int value;
    private String desc;

    AccountType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * @param value
     * @return
     */
    public static AccountType fromValue(int value) {
        for (AccountType accountType : AccountType.values()) {
            if (accountType.getValue() == value) {
                return accountType;
            }
        }

        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
