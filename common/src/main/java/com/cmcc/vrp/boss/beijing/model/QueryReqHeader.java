package com.cmcc.vrp.boss.beijing.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/8/11.
 */
@XStreamAlias("Header")
public class QueryReqHeader {

    @XStreamAlias("ChannelID")
    String channelId;

    @XStreamAlias("RequestTime")
    String requstTime;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getRequstTime() {
        return requstTime;
    }

    public void setRequstTime(String requstTime) {
        this.requstTime = requstTime;
    }
}
