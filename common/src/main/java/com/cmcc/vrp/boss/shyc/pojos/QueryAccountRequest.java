package com.cmcc.vrp.boss.shyc.pojos;

import com.cmcc.vrp.boss.shyc.annotations.NeedSign;
import com.cmcc.vrp.boss.shyc.annotations.SignTarget;

/**
 * 上海月呈定义的查询余额的请求
 *
 * Created by sunyiwei on 2017/3/14.
 */
public class QueryAccountRequest {
    private final String action = "getBalance";
    private final String v = "1.2";

    @NeedSign
    private String account;

    @NeedSign
    private String timestamp;

    @SignTarget
    private String sign;

    public String getAction() {
        return action;
    }

    public String getV() {
        return v;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
