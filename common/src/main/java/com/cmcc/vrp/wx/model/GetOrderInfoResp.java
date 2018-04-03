package com.cmcc.vrp.wx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年3月9日 下午3:34:01
*/
@XStreamAlias("GetOrderInfoResp")
public class GetOrderInfoResp {
    @XStreamAlias("Result")
    String result;
    
    @XStreamAlias("ResultMsg")
    String resultMsg;
    
    @XStreamAlias("Member")
    Member member;

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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    
}
