package com.cmcc.vrp.boss.hebei.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/10/12.
 */
@XStreamAlias("WBQryAsynDealResultRsp")
public class HbWBQryAsynDealResultRsp {

    @XStreamAlias("ERRCODE")
    private String errCode;

    @XStreamAlias("ERRINFO")
    private String errInfo;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }
}
