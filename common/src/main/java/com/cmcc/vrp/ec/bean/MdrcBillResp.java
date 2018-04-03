package com.cmcc.vrp.ec.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月15日 下午12:12:48
*/
@XStreamAlias("Response")
public class MdrcBillResp {

    @XStreamAlias("Datetime")
    String responseTime;

    @XStreamAlias("Bill")
    List<MdrcBillRespData> mdrcBillRespData;

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public List<MdrcBillRespData> getMdrcBillRespData() {
        return mdrcBillRespData;
    }

    public void setMdrcBillRespData(List<MdrcBillRespData> mdrcBillRespData) {
        this.mdrcBillRespData = mdrcBillRespData;
    }
    
    
}
