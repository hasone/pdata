package com.cmcc.vrp.boss.shyc.pojos;

/**
 * 上海月呈定义的回调响应对象
 *
 * Created by sunyiwei on 2017/3/14.
 */
public class ShycCallbackResp {
    public static final ShycCallbackResp SUCCESS = new ShycCallbackResp("success");

    public static final ShycCallbackResp FAIL = new ShycCallbackResp("fail");

    private String message;

    public ShycCallbackResp(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
