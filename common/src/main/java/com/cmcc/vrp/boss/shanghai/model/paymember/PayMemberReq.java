package com.cmcc.vrp.boss.shanghai.model.paymember;

import com.cmcc.vrp.boss.shanghai.model.common.PubInfo;
import com.cmcc.vrp.boss.shanghai.model.common.Request;

/**
 * Created by lilin on 2016/8/23.
 */
public class PayMemberReq {

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
