package com.cmcc.vrp.boss.hebei.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/10/11.
 */
@XStreamAlias("WBNetQryNewBalanceReq")
public class WBNetQryNewBalanceReq {

    @XStreamAlias("AtsvNum")
    private String atsvNum;

    @XStreamAlias("TouchOid")
    private String touchOid;

    @XStreamAlias("ChannelId")
    private String channelId;

    @XStreamAlias("DONORNUM")
    private String donorNum;

    @XStreamAlias("OID")
    private String oid;

    public String getAtsvNum() {
        return atsvNum;
    }

    public void setAtsvNum(String atsvNum) {
        this.atsvNum = atsvNum;
    }

    public String getTouchOid() {
        return touchOid;
    }

    public void setTouchOid(String touchOid) {
        this.touchOid = touchOid;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getDonorNum() {
        return donorNum;
    }

    public void setDonorNum(String donorNum) {
        this.donorNum = donorNum;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}
