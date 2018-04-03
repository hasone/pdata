package com.cmcc.vrp.ec.bean.individual;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月14日 下午1:47:35
*/
@XStreamAlias("Response")
public class MemberInqueryResp {

    @XStreamAlias("Datetime")
    String responseTime;

    @XStreamAlias("OutData")
    MemberInqueryRespData memberInqueryRespData;

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public MemberInqueryRespData getMemberInqueryRespData() {
        return memberInqueryRespData;
    }

    public void setMemberInqueryRespData(MemberInqueryRespData memberInqueryRespData) {
        this.memberInqueryRespData = memberInqueryRespData;
    }
    
    
}
