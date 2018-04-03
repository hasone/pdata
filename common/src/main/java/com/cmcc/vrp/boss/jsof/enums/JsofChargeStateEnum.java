package com.cmcc.vrp.boss.jsof.enums;

/**
 * 北京云漫的充值状态枚举
 *
 * Created by sunyiwei on 2017/4/6.
 */
public enum JsofChargeStateEnum {
    SUCCESS("1", "充值成功"),
    FAIL("9", "充值失败"),
    PROCESS("0", "充值中");

    private String code;
    private String message;

    JsofChargeStateEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据code获取相应的状态量
     *
     * @param code 状态码
     */
    public static JsofChargeStateEnum fromCode(String code) {
        for (JsofChargeStateEnum jsofChargeStateEnum : JsofChargeStateEnum.values()) {
            if (jsofChargeStateEnum.getCode().equals(code)) {
                return jsofChargeStateEnum;
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
