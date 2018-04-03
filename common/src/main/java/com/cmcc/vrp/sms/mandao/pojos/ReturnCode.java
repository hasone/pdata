package com.cmcc.vrp.sms.mandao.pojos;

public enum ReturnCode {
    INCORRECT_PASSWORD("-2", "帐号/密码不正确"),
    INSUFFICIENT_FUND("-4", "余额不足支持本次发送"),
    DATA_FORMAT_ERROR("-5", "数据格式错误"),
    PARAM_ERROR("-6", "参数有误"),
    UNAUTHORIZED("-7", "权限受限"),
    FLOW_CONTROL_ERROR("-8", "流量控制错误"),
    EXT_UNAUTHORIZED("-9", "扩展码权限错误"),
    EXCEED_MAX_LENGTH("-10", "内容长度太长"),
    INTERNAL_DB_ERROR("-11", "内部数据库错误"),
    SN_STATUS_ERROR("-12", "序列号状态错误"),
    SERVER_WRITE_FILE_ERROR("-14", "服务器写文件失败"),
    UNSUPPORTED("-17", "没有权限"),
    UNAUTHORIZED_ADDR("-19", "禁止同时使用多个接口地址"),
    DUPLICATE_ERROR("-20", "相同手机号，相同内容重复提交"),
    UNAUTHORIZED_IP("-22", "IP鉴权失败"),
    SN_NOT_FOUND("-23", "缓存无此序列号信息"),
    SN_EMPTY("-601", "序列号为空，参数错误"),
    SN_FORMAT_ERROR("-602", "序列号格式错误，参数错误"),
    PWD_EMPTY("-603", "密码为空，参数错误"),
    MOBILE_EMPTY("-604", "手机号码为空，参数错误"),
    CONTENT_EMPTY("-605", "内容为空，参数错误"),
    EXT_PARAM_ERROR("-606", "ext长度大于9，参数错误"),
    INDIGIT_EXT_PARAM("-607", "参数错误 扩展码非数字 "),
    INVALID_SEND_TIME("-608", "参数错误 定时时间非日期格式"),
    INVALID_RRID_PARAM("-609", "rrid长度大于18,参数错误 "),
    INDIGIT_RRID_PARAM("-610", "参数错误 rrid非数字"),
    INVALID_ENCODED_FORMAT("-611", "参数错误 内容编码不符合规范"),
    MOBILE_CONTENT_UNMATCH("-623", "手机个数与内容个数不匹配"),
    EXT_MOBILE_UNMATCH("-624", "扩展个数与手机个数数不符"),
    STIME_MOBILE_UNMATCH("-625", "定时时间个数与手机个数数不匹配"),
    RRID_MOBILE_UNMATCH("-626", "rrid个数与手机个数数不匹配");


    private String code;
    private String description;

    ReturnCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ReturnCode fromCode(String code) {
        for (ReturnCode returnCode : ReturnCode.values()) {
            if (returnCode.getCode().equals(code)) {
                return returnCode;
            }
        }

        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
