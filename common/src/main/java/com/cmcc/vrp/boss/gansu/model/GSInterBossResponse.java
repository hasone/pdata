package com.cmcc.vrp.boss.gansu.model;

/**
 * 甘肃BOSS充值接口返回的根对象
 * <p>
 * Created by sunyiwei on 2016/5/26.
 */
public class GSInterBossResponse {
    private GSChargeResponse interBOSS;

    public GSChargeResponse getInterBOSS() {
        return interBOSS;
    }

    public void setInterBOSS(GSChargeResponse interBOSS) {
        this.interBOSS = interBOSS;
    }
}
