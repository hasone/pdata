package com.cmcc.vrp.enums;

/**
 * 潜在客户意愿级别
 * <p>
 * Created by sunyiwei on 16-3-28.
 */
public enum PotentialCustomerPriority {
    TOP_MOST(5, "非常高"),
    TOP(4, "高"),
    MEDIUM(3, "较高"),
    LOW(2, "一般"),
    LOW_MOST(1, "低");

    private int value;
    private String desc;

    PotentialCustomerPriority(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * @param value
     * @return
     */
    public static PotentialCustomerPriority fromValue(int value) {
        for (PotentialCustomerPriority potentialCustomerPriority : PotentialCustomerPriority.values()) {
            if (potentialCustomerPriority.getValue() == value) {
                return potentialCustomerPriority;
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
