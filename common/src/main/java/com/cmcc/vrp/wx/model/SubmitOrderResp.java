package com.cmcc.vrp.wx.model;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月6日 下午4:46:56
*/
@XStreamAlias("SubmitOrderResp")
public class SubmitOrderResp extends AbstractBossOperationResultImpl{
    
    @XStreamAlias("OrderId")
    private String orderId;
    
    @XStreamAlias("Member")
    private Member member;

    public SubmitOrderResp(String resultCode, String resultMsg) {
        Member member = new Member();
        member.setResultCode(resultCode);
        member.setResultMsg(resultMsg);
        this.member = member;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    
    @Override
    public String getResultDesc() {
        if (member == null
                || member.getResultMsg() == null) {
            return "";
        }
        return member.getResultMsg();
    }
    
    @Override
    public String getResultCode() {
        if (member == null
                || member.getResultCode() == null) {
            return "";
        }
        return member.getResultCode();
    }

    @Override
    public boolean isSuccess() {
        if (member == null
                || member.getResultCode() == null
                || !"0".equals(member.getResultCode())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isAsync() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object getOperationResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isNeedQuery() {
        // TODO Auto-generated method stub
        return false;
    }

}
