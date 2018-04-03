package com.cmcc.vrp.sms.opt.enums;

/**
 * 开放平台短信通道响应类型
 *
 * Created by sunyiwei on 2017/2/13.
 */
public enum OptMsgRespEnum {
    OK("200", "发送成功"),
    MOBILE_BLACK_LIST("201", "接收手机号在黑名单中"),
    IP_BLACK_LIST("202", "发起能力调用的IP处于黑名单中"),
    PARAM_INVALID_PARAM("203", "能力调用的参数不合法"),
    KEY_INVALID_PARAM("204", "能力调用的key不合法"),
    FILTER_NOT_PASS("205", "流量过滤通不过"),
    BODY_INVALID_PARAM("400", "能力调用的消息体不合法"),
    ERROR("500", "内部错误");

    private String code;
    private String message;

    OptMsgRespEnum(String code, String message) {
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
