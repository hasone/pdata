package com.cmcc.vrp.wx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年3月7日 上午9:04:16
*/
@XStreamAlias("LECOrderRelationNotifyResp")
public class LECOrderRelationNotifyResp {
    @XStreamAlias("Result")
    String result;
    
    @XStreamAlias("ResultMsg")
    String resultMsg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
       
}
