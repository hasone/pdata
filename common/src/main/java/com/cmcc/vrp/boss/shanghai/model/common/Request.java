package com.cmcc.vrp.boss.shanghai.model.common;


/**
 * Created by lilin on 2016/8/23.
 */
public class Request {

    private String BusiCode;
    private BusiParams BusiParams;

    public String getBusiCode() {
        return BusiCode;
    }

    public void setBusiCode(String busiCode) {
        BusiCode = busiCode;
    }

    public BusiParams getBusiParams() {
        return BusiParams;
    }

    public void setBusiParams(BusiParams busiParams) {
        BusiParams = busiParams;
    }
}
