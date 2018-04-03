package com.cmcc.vrp.boss.shanghai.model.querycount;

/**
 * @author lgk8023
 *
 */
public class QueryUsableBalanceOfFlowReq {
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
