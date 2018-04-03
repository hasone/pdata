package com.cmcc.vrp.boss.shyc.pojos;

import com.cmcc.vrp.boss.shyc.annotations.NeedSign;
import com.cmcc.vrp.boss.shyc.annotations.SignTarget;

/**
 * 上海月呈定义的查询订单状态的请求对象
 *
 * Created by sunyiwei on 2017/3/14.
 */
public class QueryStatusRequest {
    private final String action = "getReports";
    private final String v = "1.2";

    @NeedSign
    private String account;

    @NeedSign
    private String orderNo;

    @NeedSign
    private int count;

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
