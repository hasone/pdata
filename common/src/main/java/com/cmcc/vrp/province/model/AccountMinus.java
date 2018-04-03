package com.cmcc.vrp.province.model;

/**
 * Created by sunyiwei on 2016/9/7.
 */
public class AccountMinus {
    private Long id;
    private Double delta;
    private AccountRecord accountRecord;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDelta() {
        return delta;
    }

    public void setDelta(Double delta) {
        this.delta = delta;
    }

    public AccountRecord getAccountRecord() {
        return accountRecord;
    }

    public void setAccountRecord(AccountRecord accountRecord) {
        this.accountRecord = accountRecord;
    }
}
