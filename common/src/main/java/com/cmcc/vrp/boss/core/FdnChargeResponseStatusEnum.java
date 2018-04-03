package com.cmcc.vrp.boss.core;

/**
 * 网宿渠道响应码
 * <p>
 * Created by sunyiwei on 2016/10/13.
 */
public enum FdnChargeResponseStatusEnum {
    OK("20000", "订购成功"),
    NULL_ORDER_REQ("40001", "订购请求不能为空"),
    INVALID_APP_KEY("40002", "无效的AppKey"),
    INVALID_TRANS_ID("40003", "无效的TransId"),
    INVALID_USERDATA("40004", "无效的用户数据"),
    INVALID_REQUEST_TIME("40005", "无效的请求时间"),
    INVALID_OPER_SEQ("50001", "无效的OperSeq"),
    SERVER_INTERNAL_ERROR("50002", "服务器内部错误");

    private String status;
    private String errDesc;

    FdnChargeResponseStatusEnum(String status, String errDesc) {
        this.status = status;
        this.errDesc = errDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }
}
