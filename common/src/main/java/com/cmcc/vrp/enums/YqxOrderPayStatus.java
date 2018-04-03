package com.cmcc.vrp.enums;

public enum YqxOrderPayStatus {
    WAIT(0, "待支付"),
    NO(1, "未支付"),//超时未支付的订单关闭，无法支付
    SUCCESS(2, "支付成功"),
    FAIL(3, "支付失败");

    private int code;

    private String type;

    private YqxOrderPayStatus(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
