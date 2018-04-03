package com.cmcc.vrp.boss.shyc.enums;

/**
 * 上海月呈定义的错误码
 *
 * Created by sunyiwei on 2017/3/14.
 */
public enum ErrorCode {
    SUCCESS("0", "成功"),
    PARAM_ERROR("001", "参数错误"),
    ILLEGAL_NO("002", "充值号码不合法"),
    ACT_PWD_ERROR("003", "账号密码错误"),
    INSUFFCIENT_ACT("004", "代理商/上级代理商余额不足"),
    AUTH_ERROR("005", "代理商或客户无此流量包权限"),
    UNSUPPORT_DISC("006", "不支持该地区"),
    CARD_NO_PWD_ERROR("007", "卡号或者密码错误"),
    CARD_IS_USED("008", "该卡已经被使用"),
    UNSUPPORT_NO("009", "该卡不支持(移动/联通/电信)号码"),
    INVALID_CHANNEL("010", "代理商未分配可用的通道"),
    INVALID_PARAM("011", "卡流量包参数不正确"),
    INVALID_DATE("012", "该卡不在可用日期内"),
    DUPLICATE_SN("013", "已存在重复订单号"),
    EXCEED_LIMIT("014", "该号码在24小时内已达到充值上限次数"),
    UNSUPPORT_PACKAGE("015", "代理商分配的通道,不支持流量包"),
    NO_EXIST_PACKAGE("016", "移动/联通/电信号码未指定流量包或指定的流量包不存在"),
    NOT_ISSUE("020", "该卡没有启用或没有发行"),
    TOO_MANY("030", "充值过于频繁,请稍后再试"),
    SIGN_ERROR("100", "签名验证错误"),
    PROXY_STOP_OR_DELETED("101", "代理商已停用或删除"),
    ILLEGAL_IP("102", "IP地址非法"),
    PROXY_AUTH_ERROR("103", "代理商无接口权限"),
    PHONE_REGION_ERROR("150", "无法找到对应的归属地"),
    INVALID_TIMESTAMP("213", "时间戳失效"),
    OTHERS_ERROR("999", "其它错误");

    private String code;
    private String message;

    ErrorCode(String code, String message) {
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
