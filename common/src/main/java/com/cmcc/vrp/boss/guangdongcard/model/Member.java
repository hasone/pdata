package com.cmcc.vrp.boss.guangdongcard.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.LinkedList;

/**
 * 手机号码或成员标识
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
@XStreamAlias("Member")
public class Member {
    @XStreamAlias("OptType")
    private String optType;

    @XStreamAlias("Mobile")
    private String mobile;

    @XStreamAlias("MemberName")
    private String memberName;

    @XStreamAlias("Smsconfirm")
    private String smsConfirm;

    @XStreamAlias("Products")
    private LinkedList<Product> products;

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getSmsConfirm() {
        return smsConfirm;
    }

    public void setSmsConfirm(String smsConfirm) {
        this.smsConfirm = smsConfirm;
    }

    public LinkedList<Product> getProducts() {
        return products;
    }

    public void setProducts(LinkedList<Product> products) {
        this.products = products;
    }
}
