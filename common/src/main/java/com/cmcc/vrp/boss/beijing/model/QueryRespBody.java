package com.cmcc.vrp.boss.beijing.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/8/11.
 */
@XStreamAlias("WebBody")
public class QueryRespBody {

    @XStreamAlias("RetInfo")
    private QueryRetInfo queryRetInfo;

    public QueryRetInfo getQueryRetInfo() {
        return queryRetInfo;
    }

    public void setQueryRetInfo(QueryRetInfo queryRetInfo) {
        this.queryRetInfo = queryRetInfo;
    }
}
