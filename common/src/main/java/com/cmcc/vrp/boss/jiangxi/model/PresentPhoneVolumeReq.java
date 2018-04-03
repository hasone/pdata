package com.cmcc.vrp.boss.jiangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/7/7.
 */
@XStreamAlias("PresentPhoneVolumeReq")
public class PresentPhoneVolumeReq {

    @XStreamAlias("HEAD")
    private JXChargeHead chargeHead;

    @XStreamAlias("BODY")
    private String chargeBody;

    public JXChargeHead getChargeHead() {
        return chargeHead;
    }

    public void setChargeHead(JXChargeHead chargeHead) {
        this.chargeHead = chargeHead;
    }

    public String getChargeBody() {
        return chargeBody;
    }

    public void setChargeBody(String chargeBody) {
        this.chargeBody = chargeBody;
    }
}
