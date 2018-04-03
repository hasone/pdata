package com.cmcc.vrp.wx.beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月9日 下午3:51:07
*/
@XStreamAlias("Response")
public class QueryResp {
    @XStreamAlias("QueryResp")
    QueryRespData queryRespData;

    public QueryRespData getQueryRespData() {
        return queryRespData;
    }

    public void setQueryRespData(QueryRespData queryRespData) {
        this.queryRespData = queryRespData;
    }
    
}
