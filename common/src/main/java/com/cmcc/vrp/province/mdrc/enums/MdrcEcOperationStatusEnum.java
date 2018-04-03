package com.cmcc.vrp.province.mdrc.enums;

/**
 * 流量卡状态操作结果枚举类
 * <p>
 * Created by sunyiwei on 2016/5/31.
 */
public enum MdrcEcOperationStatusEnum {
    SUCCESS("10000", "操作成功"),
    PARIAL_SUCCESS("10001", "部分成功"),
    FAILED("10002", "操作失败");

    private String code;
    private String message;

    MdrcEcOperationStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
