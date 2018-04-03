package com.cmcc.vrp.boss.bjym.pojos;

/**
 * 北京云漫回调请求对象
 *
 * Created by sunyiwei on 2017/4/6.
 */
public class BjymCallbackRequest {
    private String partyId;
    private BjymCallbackRequestData data;
    private String time;
    private String sign;

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public BjymCallbackRequestData getData() {
        return data;
    }

    public void setData(BjymCallbackRequestData data) {
        this.data = data;
    }
}
