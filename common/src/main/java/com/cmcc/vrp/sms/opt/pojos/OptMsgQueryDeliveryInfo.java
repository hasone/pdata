package com.cmcc.vrp.sms.opt.pojos;

/**
 * 开放平台短信发送状态对象
 *
 * Created by sunyiwei on 2017/2/13.
 */
public class OptMsgQueryDeliveryInfo {
    private String destAddr;
    private String statusCode;

    public String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "OptMsgQueryDeliveryInfo{" +
                "destAddr='" + destAddr + '\'' +
                ", statusCode='" + statusCode + '\'' +
                '}';
    }
}
