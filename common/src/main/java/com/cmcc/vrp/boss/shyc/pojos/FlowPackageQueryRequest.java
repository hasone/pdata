package com.cmcc.vrp.boss.shyc.pojos;

import com.cmcc.vrp.boss.shyc.annotations.NeedSign;
import com.cmcc.vrp.boss.shyc.annotations.SignTarget;

/**
 * 上海月呈定义的获取流量包定义的请求对象
 *
 * Created by sunyiwei on 2017/3/14.
 */
public class FlowPackageQueryRequest {
    private final String action = "getPackage";
    private final String v = "1.2";

    @NeedSign
    private String account;

    @NeedSign
    private String type;

    @NeedSign
    private String timeStamp;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
