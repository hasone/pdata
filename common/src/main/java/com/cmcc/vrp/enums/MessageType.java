/**
 *
 */
package com.cmcc.vrp.enums;

/**
 * @author JamieWu
 *         短信的类型
 */
public enum MessageType {
    RANDOM_CODE("1", "随机验证码"),
    CHARGE_NOTICE("2", "充值提醒"),
    FLOWCARD_NOTICE("3", "流量券通知"),
    APPROVAL_NOTICE("4", "审批通知"),
    SUPPLIER_BALANCE("5", "供应商余额"),
    MDRC_EXPIRED("6", "营销卡即将过期"),
    ENTERPRISE_OPER("7", "企业变更通知");

    private String code;

    private String name;

    private MessageType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
