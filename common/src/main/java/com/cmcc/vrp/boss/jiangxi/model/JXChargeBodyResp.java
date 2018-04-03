package com.cmcc.vrp.boss.jiangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/8/19.
 */
@XStreamAlias("BODY")
public class JXChargeBodyResp {

    @XStreamAlias("RESULTCODE")
    private String resultCode;

    @XStreamAlias("RESULTMSG")
    private String resultMsg;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
