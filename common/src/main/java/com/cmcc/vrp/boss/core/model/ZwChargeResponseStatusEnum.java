package com.cmcc.vrp.boss.core.model;

/**
 * 卓望渠道充值的返回码
 * <p>
 * Created by sunyiwei on 2016/10/13.
 */
public enum ZwChargeResponseStatusEnum {
    SUCCESS("00", "成功"),
    ILLEGAL_SIGN("10", "非法签名"),
    PARAMETER_LOST("11", "参数缺失"),
    SIGN_LOST("12", "签名缺失"),
    NON_UNIQUE_TRANSID("13", "重复的TRANSID"),
    ILLEGAL_APPKEY("14", "非法的APPKEY"),
    INVAILD_XML("15", "XML格式不正确"),
    ILLEGAL_USERDATALIST("16", "充值数量不合法"),
    REQUEST_MORE_THAN_3MINS("17", "报文产生时间超过3分钟");

    private String code;

    private String msg;

    ZwChargeResponseStatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
