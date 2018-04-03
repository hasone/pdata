package com.cmcc.vrp.enums;

/**
 * 产品变更枚举
 *
 * @author JamieWu
 */
public enum ProductChangeRequestStatus {
    NOCHANGE(0, "无变更记录或者审批完成"),
    SAVED(1, "已保存"),
    APPROVING(2, "审批中"),
    CANCEL(3, "取消申请"),
    REJECT(4, "驳回变更"),
    ONLYDETAIL(5,"只显示详情");//用于部分页面区分只有详情的情况

    private int value;
    private String desc;

    ProductChangeRequestStatus(int value, String desc) {
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
