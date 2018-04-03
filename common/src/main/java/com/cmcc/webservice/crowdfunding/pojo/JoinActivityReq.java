package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * JoinActivityReq.java
 * 广东众筹活动报名接口，请求对象
 * @author wujiamin
 * @date 2017年2月8日
 */
@XStreamAlias("Request")
public class JoinActivityReq {
    @XStreamAlias("Join")
    JoinActivityReqData joinData;

    @XStreamAlias("Datetime")
    String requestTime;

    public JoinActivityReqData getJoinData() {
        return joinData;
    }

    public void setJoinData(JoinActivityReqData joinData) {
        this.joinData = joinData;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}
