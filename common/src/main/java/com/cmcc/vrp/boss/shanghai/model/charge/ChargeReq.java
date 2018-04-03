package com.cmcc.vrp.boss.shanghai.model.charge;

import com.cmcc.vrp.boss.shanghai.model.common.PubInfo;
import com.cmcc.vrp.boss.shanghai.model.common.Request;

/**
 * @author lgk8023
 *
 */
public class ChargeReq {
    private PubInfo PubInfo;

    private Request Request;

    public com.cmcc.vrp.boss.shanghai.model.common.PubInfo getPubInfo() {
        return PubInfo;
    }

    public void setPubInfo(com.cmcc.vrp.boss.shanghai.model.common.PubInfo pubInfo) {
        PubInfo = pubInfo;
    }

    public com.cmcc.vrp.boss.shanghai.model.common.Request getRequest() {
        return Request;
    }

    public void setRequest(com.cmcc.vrp.boss.shanghai.model.common.Request request) {
        Request = request;
    }
}
