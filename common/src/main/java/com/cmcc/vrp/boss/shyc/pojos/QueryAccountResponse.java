package com.cmcc.vrp.boss.shyc.pojos;

/**
 * 上海月呈定义的查询余额响应对象
 *
 * Created by sunyiwei on 2017/3/14.
 */
public class QueryAccountResponse {
    private String code;
    private String message;
    private double balance;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
