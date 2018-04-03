package com.cmcc.vrp.enums;

/**
 * Created by sunyiwei on 2016/4/19.
 */
public enum AccountChangeApprovalOperationResult {
    COMMIT(0, "提交申请"),
    APPROVAL(1, "申请通过"),
    REJECT(2, "驳回申请");

    private int value;
    private String desc;

    AccountChangeApprovalOperationResult(int value, String desc) {
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
