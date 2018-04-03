package com.cmcc.vrp.boss.gd.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("QryECSyncInfoReq")
public class QryECSyncInfoReq {

    @XStreamAlias("ECCode")
    private String ecCode;

    public String getEcCode() {
        return ecCode;
    }

    public void setEcCode(String ecCode) {
        this.ecCode = ecCode;
    }
}
