package com.cmcc.vrp.boss.guangdong.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/7/11.
 */
@XStreamAlias("Member")
public class GdMember {

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
    private PrdList1 prdList1;

    @XStreamAlias("PrdList")
    private PrdList2 prdList2;

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

    public PrdList1 getPrdList1() {
        return prdList1;
    }

    public void setPrdList1(PrdList1 prdList1) {
        this.prdList1 = prdList1;
    }

    public PrdList2 getPrdList2() {
        return prdList2;
    }

    public void setPrdList2(PrdList2 prdList2) {
        this.prdList2 = prdList2;
    }
}
