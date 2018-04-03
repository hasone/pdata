package com.cmcc.vrp.boss.shangdong.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/3/31.
 */
@XStreamAlias("PubInfo")
public class OrderRespPubInfo {

    @XStreamAlias("ReturnCode")
    String returnCode;

    @XStreamAlias("ReturnMsg")
    String returnMsg;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
}
