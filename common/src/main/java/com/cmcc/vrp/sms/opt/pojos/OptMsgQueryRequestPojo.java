package com.cmcc.vrp.sms.opt.pojos;

/**
 * 开放平台短信状态查询请求参数
 *
 * Created by sunyiwei on 2017/2/13.
 */
public class OptMsgQueryRequestPojo {
    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "OptMsgQueryRequestPojo{" +
                "messageId='" + messageId + '\'' +
                '}';
    }
}
