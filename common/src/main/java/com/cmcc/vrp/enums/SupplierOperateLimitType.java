package com.cmcc.vrp.enums;

/**
 * 供应商修改限额操作类型
 * Created by qinqinyan.
 */
public enum SupplierOperateLimitType {
    NO_CHANGE(0, "不做更改"),
    OPEN(1, "设置限额"),
    CLOSE(2, "取消限额"),
    MODIFY_LIMIT_MONEY(3, "变更限制额度");

    private Integer value;
    private String desc;

    SupplierOperateLimitType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * @param value
     * @return
     */
   /* public static SupplierOperateLimitType fromValue(int value) {
        for (SupplierOperateLimitType accountType : SupplierOperateLimitType.values()) {
            if (accountType.getValue() == value) {
                return accountType;
            }
        }

        return null;
    }*/

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
