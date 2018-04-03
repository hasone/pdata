package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 广东众筹活动查询接口，请求对象
 * @author wujiamin
 * @date 2017年2月8日
 */
@XStreamAlias("Request")
public class QueryActivityReq {
    @XStreamAlias("QueryActivity")
    QueryActivityReqData data;

    @XStreamAlias("Datetime")
    String requestTime;

    public QueryActivityReqData getData() {
        return data;
    }

    public void setData(QueryActivityReqData data) {
        this.data = data;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}
