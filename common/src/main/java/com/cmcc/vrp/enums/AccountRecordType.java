package com.cmcc.vrp.enums;

/**
 * 帐户记录类型枚举类
 * <p>
 * Created by sunyiwei on 2016/4/14.
 */
public enum AccountRecordType {
    INCOME((byte) 0, "收入"),
    OUTGO((byte) 1, "支出"),
    RESET((byte) 2, "重置");

    private byte value;
    private String desc;

    AccountRecordType(byte value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public byte getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
