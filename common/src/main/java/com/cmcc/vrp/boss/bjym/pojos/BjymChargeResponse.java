package com.cmcc.vrp.boss.bjym.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * 北京云漫充值响应对象
 *
 * Created by sunyiwei on 2017/4/6.
 */
public class BjymChargeResponse {
    private String code;
    private String description;

    @SerializedName("sendid")
    private String sendId;

    @SerializedName("requestid")
    private String requestId;

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

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
