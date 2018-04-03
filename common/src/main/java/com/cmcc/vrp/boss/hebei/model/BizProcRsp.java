package com.cmcc.vrp.boss.hebei.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/10/13.
 */
@XStreamAlias("BizProcRsp")
public class BizProcRsp {

    @XStreamAlias("OID")
    private String OID;

    public String getOID() {
        return OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }
}
