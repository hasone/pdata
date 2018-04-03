package com.cmcc.vrp.boss.bjym.enums;

/**
 * 北京云漫充值响应状态码
 *
 * Created by sunyiwei on 2017/4/6.
 */
public enum BjymChargeResponseCodeEnum {
    OK("0", "发送成功"),
    PRD_NOT_EXIT("1", "产品不存在"),
    PRD_OFFLINE("2", "产品售完或已下架"),
    GATEWAY_CONN_ERROR("3", "网关通讯错误"),
    GATEWAY_RESP_ERROR("4", "网关应答错误"),
    USR_PWD_ERROR("5", "用户名密码不正确"),
    MOBILE_ERROR("6", "手机号码不正确或者不被支持"),
    INSUFFICIENT_FUND("7", "预存余额不足"),
    MALFORMED_DATA("8", "提交数据格式不正确"),
    UNAUTHORIZED_IP("9", "非法IP源"),
    OTHERS("10", "其它问题"),
    SIGN_ERROR("11", "签名验证不合法"),
    PARAM_ERROR("12", "请填写正确的参数信息"),
    SUBMIT_ERROR("13", "数据提交异常"),
    MOBILE_PACKAGE_EMPTY("14", "流量包或者手机号为空"),
    UNAUTHORIZED_BUSI("15", "此业务暂未开通"),
    MOBILE_IN_BLACK_LIST("16", "此号码为黑名单"),
    UNSUPPORTED_METHOD("17", "不支持的提交方式"),
    EMPTY_ORDER_NO("18", "订单号为空"),
    INVALID_PARAM("9999", "无效的参数(平台自定义)");

    private String code;
    private String message;

    BjymChargeResponseCodeEnum(String code, String message) {
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
