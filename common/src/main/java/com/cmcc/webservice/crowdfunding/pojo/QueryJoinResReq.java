package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 广东众筹报名结果查询，请求对象
 * QueryJoinResReq.java
 * @author wujiamin
 * @date 2017年2月9日
 */
@XStreamAlias("Request")
public class QueryJoinResReq {
    @XStreamAlias("QueryJoinRes")
    QueryJoinResReqData data;

    @XStreamAlias("Datetime")
    String requestTime;

    public QueryJoinResReqData getData() {
        return data;
    }

    public void setData(QueryJoinResReqData data) {
        this.data = data;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }    
}
