package com.cmcc.vrp.wx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月5日 上午8:46:45
*/
@XStreamAlias("Member")
public class Member {
    
    @XStreamAlias("OptType")
    private String optType;

    @XStreamAlias("PayFlag")
    private String payFlag;

    @XStreamAlias("UsecyCle")
    private String usecyCle;
    
    @XStreamAlias("Mobile")
    private String mobile;

    @XStreamAlias("UserName")
    private String userName;

    @XStreamAlias("EffType")
    private String effType;
    
    @XStreamAlias("PrdList")
    private PrdList prdList2;
    
    @XStreamAlias("PrdList")
    private PrdList prdList1;
    
    @XStreamAlias("PrdList")
    private PrdList prdList3;

    @XStreamAlias("ResultCode")
    private String resultCode;
    
    @XStreamAlias("CRMApplyCode")
    private String cRMApplyCode;
    
    @XStreamAlias("ResultMsg")
    private String resultMsg;
    
    @XStreamAlias("OrderId")
    private String orderId;
   
    
    public String getOrderId() {
        return orderId;
    }


    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public String getOptType() {
        return optType;
    }


    public void setOptType(String optType) {
        this.optType = optType;
    }


    public String getPayFlag() {
        return payFlag;
    }


    public void setPayFlag(String payFlag) {
        this.payFlag = payFlag;
    }


    public String getUsecyCle() {
        return usecyCle;
    }


    public void setUsecyCle(String usecyCle) {
        this.usecyCle = usecyCle;
    }


    public String getMobile() {
        return mobile;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getEffType() {
        return effType;
    }


    public void setEffType(String effType) {
        this.effType = effType;
    }


    public PrdList getPrdList1() {
        return prdList1;
    }


    public void setPrdList1(PrdList prdList1) {
        this.prdList1 = prdList1;
    }


    public PrdList getPrdList2() {
        return prdList2;
    }


    public void setPrdList2(PrdList prdList2) {
        this.prdList2 = prdList2;
    }


    public PrdList getPrdList3() {
        return prdList3;
    }


    public void setPrdList3(PrdList prdList3) {
        this.prdList3 = prdList3;
    }


    public String getResultCode() {
        return resultCode;
    }


    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }


    /**
     * @return
     */
    public String getcRMApplyCode() {
        return cRMApplyCode;
    }


    /**
     * @param cRMApplyCode
     */
    public void setcRMApplyCode(String cRMApplyCode) {
        this.cRMApplyCode = cRMApplyCode;
    }


    public String getResultMsg() {
        return resultMsg;
    }


    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
    
    
}
