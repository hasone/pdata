package com.cmcc.vrp.boss.shanghai.model.queryproduct;

/**
 * Created by lilin on 2016/8/25.
 */
public class QueryProductReq {

    private com.cmcc.vrp.boss.shanghai.model.common.PubInfo PubInfo;

    private com.cmcc.vrp.boss.shanghai.model.common.Request Request;

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
