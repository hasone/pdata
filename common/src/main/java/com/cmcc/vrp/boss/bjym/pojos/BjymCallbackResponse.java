package com.cmcc.vrp.boss.bjym.pojos;

/**
 * 回调响应对象
 *
 * Created by sunyiwei on 2017/4/6.
 */
public class BjymCallbackResponse {
    public static BjymCallbackResponse SUCCESS = new BjymCallbackResponse("ok");
    public static BjymCallbackResponse FAIL = new BjymCallbackResponse("other");
    private final String message;

    public BjymCallbackResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
