package com.cmcc.vrp.sms.opt.pojos;

/**
 * 开放平台短信通道请求对象
 *
 * Created by sunyiwei on 2017/2/13.
 */
public class OptMsgSendRequestPojo {
    private String destAddr;
    private String message;
    private int needReceipt;
    private String apiKey;
    private String secretKey;
    private String receiptNotificationURL;

    public String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNeedReceipt() {
        return needReceipt;
    }

    public void setNeedReceipt(int needReceipt) {
        this.needReceipt = needReceipt;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getReceiptNotificationURL() {
        return receiptNotificationURL;
    }

    public void setReceiptNotificationURL(String receiptNotificationURL) {
        this.receiptNotificationURL = receiptNotificationURL;
    }

    @Override
    public String toString() {
        return "OptMsgSendRequestPojo{" +
                "destAddr='" + destAddr + '\'' +
                ", message='" + message + '\'' +
                ", needReceipt=" + needReceipt +
                ", apiKey='" + apiKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", receiptNotificationURL='" + receiptNotificationURL + '\'' +
                '}';
    }
}
