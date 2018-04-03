/**
 *
 */
package com.cmcc.webservice.sichuan.pojo;

/**
 * @author JamieWu
 *         接口返回码
 */
public enum ReturnCode {
    success("0000", "成功"),
    parameter_error("0102", "参数错误"),
    invalid_sign("0103", "校验失败"),
    invalid_account("0104", "账号不存在"),
    invalid_ip("0105", "Ip签权失败"),
    invalid_record("0106", "记录不存在"),
    invalid_moblie("0108", "含有无效同步手机号码"),
    synchronization_mobile_limit_error("0109", "手机号码同步超过限制"),
    request_more_than_5min("0110", "时间戳过期"),
    inner_error("0098", "处理方内部错误"),
    other_error("0099", "其他错误"),
    query_serial_num_error("0100", "查询序列号异常"),
    no_product("0111", "产品不存在"),
    ACCOUNT_CHECK_DATE_ERROR("0999", "对账日期间无法充值"),
    over_date_range("0112", "用户网龄小于1天"),
    no_date_range("0113", "v网网龄查询失败"),
    no_response("0114", "返回为空");
    private String code;
    private String msg;

    ReturnCode(String code, String msg) {
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
