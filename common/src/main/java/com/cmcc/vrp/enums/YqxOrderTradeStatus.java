package com.cmcc.vrp.enums;

public enum YqxOrderTradeStatus {
    PROCESSING(0, "交易中"),
    CLOSE(1, "交易关闭"),//超时未支付的订单关闭
    SUCCESS(2, "交易成功"),
    FAIL(3, "交易失败");

    private int code;

    private String type;

    private YqxOrderTradeStatus(int code, String type) {
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
