package com.cmcc.vrp.boss.hebei.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/28.
 */
@XStreamAlias("Response")
public class Response {

    @XStreamAlias("RspType")
    private String RspType;
    @XStreamAlias("RspCode")
    private String RspCode;
    @XStreamAlias("RspDesc")
    private String RspDesc;

    public String getRspType() {
        return RspType;
    }

    public void setRspType(String rspType) {
        RspType = rspType;
    }

    public String getRspCode() {
        return RspCode;
    }

    public void setRspCode(String rspCode) {
        RspCode = rspCode;
    }

    public String getRspDesc() {
        return RspDesc;
    }

    public void setRspDesc(String rspDesc) {
        RspDesc = rspDesc;
    }
}
