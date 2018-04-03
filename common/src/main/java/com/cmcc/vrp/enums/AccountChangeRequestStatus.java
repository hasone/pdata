package com.cmcc.vrp.enums;

/**
 * 帐户变更申请状态枚举
 * <p>
 * Created by sunyiwei on 2016/4/19.
 */
public enum AccountChangeRequestStatus {
    SAVED(0, "已保存"),
    APPROVING_ONE(1, "待市级管理员审批"),
    APPROVING_TWO(2, "待省级管理员审批"),
    APPROVING_THREE(3, "待省级管理员审批"),
    APPROVED(4, "审批通过"),
    REJECT(5, "审批驳回"),
    CANCEL(6, "取消申请");

    private int value;
    private String desc;

    AccountChangeRequestStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
