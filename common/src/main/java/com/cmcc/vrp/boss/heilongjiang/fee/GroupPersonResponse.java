package com.cmcc.vrp.boss.heilongjiang.fee;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * { "resCode":"Ezga802", "resMsg":"转账金额0元或充值个数小于零,不允许转账", "boss_order_no":"0" }
 * 
 * @author Lenovo
 *
 */
public class GroupPersonResponse {
    private String resCode;
    private String resMsg;
    private String bossOrderNo;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getBossOrderNo() {
        return bossOrderNo;
    }

    @JsonProperty("boss_order_no")
    public void setBoss_order_no(String bossOrderNo) {
        this.bossOrderNo = bossOrderNo;
    }

    @Override
    public String toString() {
        return "GroupPersonResponse [resCode=" + resCode + ", resMsg=" + resMsg + ", bossOrderNo=" + bossOrderNo + "]";
    }
}
