package com.cmcc.vrp.enums;

/**
 * Created by leelyn on 2016/12/8.
 */
public enum ProductMaxValue {

    HLJ_MOBILE_FEE(50000, "黑龙江花费赠送单笔最大值(分)"),
    XJ_FLOW_POOL(11264, "新疆流量池单笔赠送最大值(M)");

    private Integer value;

    private String msg;

    ProductMaxValue(Integer value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
