package com.cmcc.vrp.sms.opt.pojos;

/**
 * 开放平台短信通道发送短信响应对象
 *
 * Created by sunyiwei on 2017/2/13.
 */
public class OptMsgSendResponsePojo {
    private String resultCode;
    private String resultMsg;
    private String messageID;

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

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    @Override
    public String toString() {
        return "OptMsgSendResponsePojo{" +
                "resultCode='" + resultCode + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                ", messageID='" + messageID + '\'' +
                '}';
    }
}
