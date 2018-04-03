package com.cmcc.vrp.sms.opt.pojos;

/**
 * 开放平台短信查询响应对象
 *
 * Created by sunyiwei on 2017/2/13.
 */
public class OptMsgQueryResponsePojo {
    private String resultCode;
    private String resultMsg;
    private OptMsgQueryDeliveryInfo deliveryInfo;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public OptMsgQueryDeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(OptMsgQueryDeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    @Override
    public String toString() {
        return "OptMsgQueryResponsePojo{" +
                "resultCode='" + resultCode + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                ", deliveryInfo=" + deliveryInfo +
                '}';
    }
}
