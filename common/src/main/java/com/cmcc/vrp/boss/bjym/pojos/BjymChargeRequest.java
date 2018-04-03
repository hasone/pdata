package com.cmcc.vrp.boss.bjym.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * 北京云漫的充值请求 Created by sunyiwei on 2017/4/6.
 */
public class BjymChargeRequest {
    @SerializedName("header")
    private BjymRequestHeader requestHeader;

    @SerializedName("body")
    private BjymRequestBody requestBody;

    public BjymRequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(BjymRequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public BjymRequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(BjymRequestBody requestBody) {
        this.requestBody = requestBody;
    }
}
