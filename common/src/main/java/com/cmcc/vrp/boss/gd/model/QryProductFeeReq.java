package com.cmcc.vrp.boss.gd.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("QryProductFeeReq")
public class QryProductFeeReq {

    @XStreamAlias("ECPrdCode")
    private String ecPrdCode;

    public String getEcPrdCode() {
        return ecPrdCode;
    }

    public void setEcPrdCode(String ecPrdCode) {
        this.ecPrdCode = ecPrdCode;
    }
   
   
}
