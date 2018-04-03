package com.cmcc.vrp.wx.beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月9日 下午3:37:48
*/
@XStreamAlias("Request")
public class QueryInfoReq {
    @XStreamAlias("QueryInfo")
    QueryInfoReqData queryInfo;

    public QueryInfoReqData getQueryInfo() {
        return queryInfo;
    }

    public void setQueryInfo(QueryInfoReqData queryInfo) {
        this.queryInfo = queryInfo;
    }

}
