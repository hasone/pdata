package com.cmcc.vrp.enums;

/**
 * 个人账户记录的状态
 *
 * @author wujiamin
 * @date 2016年9月27日下午2:30:53
 */
public enum IndividualAccountType {
    INDIVIDUAL_BOSS(-1, "个人BOSS账户"),
    INDIVIDUAL_ACTIVITIES(0, "活动冻结账户"),
    INDIVIDUAL_EXCHANGE(1, "兑换冻结账户"),
    INDIVIDUAL_PURCHASE(2, "购买冻结账户");

    private Integer value;
    private String desc;

    IndividualAccountType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
