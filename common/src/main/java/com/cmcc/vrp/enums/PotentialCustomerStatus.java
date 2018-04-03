package com.cmcc.vrp.enums;

/**
 * Created by sunyiwei on 16-3-30.
 */
public enum PotentialCustomerStatus {
    ON_THE_WAY(0, "推进中"),
    DONE(1, "已签约");

    private int value;
    private String desc;

    PotentialCustomerStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * @param value
     * @return
     */
    public static PotentialCustomerStatus fromValue(int value) {
        for (PotentialCustomerStatus potentialCustomerStatus : PotentialCustomerStatus.values()) {
            if (potentialCustomerStatus.getValue() == value) {
                return potentialCustomerStatus;
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
