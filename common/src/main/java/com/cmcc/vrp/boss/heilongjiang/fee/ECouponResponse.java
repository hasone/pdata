package com.cmcc.vrp.boss.heilongjiang.fee;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * { "resCode": "Ezgam01", "resMsg": "转出用户查询dCustMsg错误[1403]",
 * "boss_correct_no": "0", "boss_correct_date": "0" }
 * 
 * @author Lenovo
 *
 */
public class ECouponResponse {
    private String resCode;
    private String resMsg;
    private String bossCorrectNo;
    private String bossCorrectDate;

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

    public String getBossCorrectNo() {
        return bossCorrectNo;
    }

    @JsonProperty("boss_correct_no")
    public void setBossCorrectNo(String bossCorrectNo) {
        this.bossCorrectNo = bossCorrectNo;
    }

    public String getBoss_correct_date() {
        return bossCorrectDate;
    }

    @JsonProperty("boss_correct_date")
    public void setBossCorrectDate(String bossCorrectDate) {
        this.bossCorrectDate = bossCorrectDate;
    }

    @Override
    public String toString() {
        return "ECouponResponse [resCode=" + resCode + ", resMsg=" + resMsg + ", boss_correct_no=" + bossCorrectNo
                + ", bossCorrectDate=" + bossCorrectDate + "]";
    }

}
