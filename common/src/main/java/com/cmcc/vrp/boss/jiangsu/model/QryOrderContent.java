package com.cmcc.vrp.boss.jiangsu.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("content")
public class QryOrderContent {

    @XStreamAlias("msisdn")
    private String msisdn;
    
    @XStreamAlias("offer_code")
    private String offerCode;
    
    @XStreamAlias("begintime")
    private String begintime;
    
    @XStreamAlias("endtime")
    private String endtime;
    
    @XStreamAlias("channelid")
    private String channelid;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }
}
