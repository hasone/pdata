package com.cmcc.vrp.ec.bean.individual;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * RedpacketCallBackReq.java
 * @author wujiamin
 * @date 2017年1月13日
 */
@XStreamAlias("Request")
public class RedpacketCallBackReq {
    @XStreamAlias("Record")
    private RedpacketCallBackReqData callBackReqData;

    public RedpacketCallBackReqData getCallBackReqData() {
        return callBackReqData;
    }

    public void setCallBackReqData(RedpacketCallBackReqData callBackReqData) {
        this.callBackReqData = callBackReqData;
    }
}
