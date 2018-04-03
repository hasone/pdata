package com.cmcc.vrp.boss.bjym.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * 北京云漫的请求头
 *
 * Created by sunyiwei on 2017/4/6.
 */
public class BjymRequestHeader {
    private String sign;
    private int partyId;

    @SerializedName("report_url")
    private String reportUrl;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }
}
