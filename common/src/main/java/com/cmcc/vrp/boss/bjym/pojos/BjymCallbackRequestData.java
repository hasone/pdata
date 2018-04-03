package com.cmcc.vrp.boss.bjym.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 北京云漫回调请求对象中的data属性
 *
 * Created by sunyiwei on 2017/4/6.
 */
public class BjymCallbackRequestData {
    private final String type = "3";
    private String size;

    @SerializedName("messageList")
    private List<BjymCallbackRequestMessage> callbackRequestMessageList;

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<BjymCallbackRequestMessage> getCallbackRequestMessageList() {
        return callbackRequestMessageList;
    }

    public void setCallbackRequestMessageList(List<BjymCallbackRequestMessage> callbackRequestMessageList) {
        this.callbackRequestMessageList = callbackRequestMessageList;
    }
}
