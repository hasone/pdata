package com.cmcc.vrp.enums;

public enum SmsBlackType {
    Enterprise_Approval("1", "企业审批"),
    Ec_Approval("2", "企业Ec审批"),
    IDENTIFYING_CODE("3","短信验证码"),
    CHARGE_NOTICE("4", "充值到账通知"),
    ALERT_NOTICE("5", "预警值提醒"),
    STOP_NOTICE("6", "暂停值提醒"),
    ACTIVITY_NOTICE("7", "营销活动通知"),
    INIT_PASSWORD("8", "初始化静态密码");


    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    SmsBlackType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
