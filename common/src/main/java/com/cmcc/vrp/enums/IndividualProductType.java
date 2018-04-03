package com.cmcc.vrp.enums;

/**
 * 个人产品类型
 *
 * @author wujiamin
 * @date 2016年9月23日下午1:33:06
 */
public enum IndividualProductType {
    PHONE_FARE(0, "话费"),
    FLOW_COIN(1, "流量币"),
    FLOW_PACKAGE(2, "流量包"),
    DEFAULT_FLOW_PACKAGE(3, "四川红包默认流量包"),
    INDIVIDUAL_POINT(4, "积分"),
    YQX_PRODUCT(5, "云企信订购产品");

    private Integer value;
    private String message;

    IndividualProductType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
