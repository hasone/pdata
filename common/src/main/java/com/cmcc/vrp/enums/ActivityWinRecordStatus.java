package com.cmcc.vrp.enums;

/**
 * Created by qinqinyan on 2016/11/25.
 * 中奖纪录状态
 */
public enum ActivityWinRecordStatus {
    UNUSE(0, "未使用"),
    WAIT(1, "待充值"),
    PROCESSING(2, "已发送充值请求"),
    SUCCESS(3, "充值成功"),
    FALURE(4, "充值失败");

    private Integer code;

    private String name;

    private String statusCode;

    ActivityWinRecordStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * @param value
     * @return
     */
    public static ActivityWinRecordStatus fromValue(Integer value) {
        if (value == null) {
            return null;
        }

        for (ActivityWinRecordStatus type : ActivityWinRecordStatus.values()) {
            if (type.getCode().equals(value)) {
                return type;
            }
        }

        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return
     */
    public String getname() {
        return name;
    }

    /**
     * @param name
     */
    public void setname(String name) {
        this.name = name;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
