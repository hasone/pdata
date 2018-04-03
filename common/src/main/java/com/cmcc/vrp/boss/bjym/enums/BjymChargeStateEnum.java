package com.cmcc.vrp.boss.bjym.enums;

/**
 * 北京云漫的充值状态枚举
 *
 * Created by sunyiwei on 2017/4/6.
 */
public enum BjymChargeStateEnum {
    SUCCESS("0", "充值成功"),
    FAIL("1", "充值失败"),
    UNKNOWN("2", "未知的充值结果,需人工排查");

    private String code;
    private String message;

    BjymChargeStateEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据code获取相应的状态量
     *
     * @param code 状态码
     */
    public static BjymChargeStateEnum fromCode(String code) {
        for (BjymChargeStateEnum bjymChargeStateEnum : BjymChargeStateEnum.values()) {
            if (bjymChargeStateEnum.getCode().equals(code)) {
                return bjymChargeStateEnum;
            }
        }

        return null;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
