package com.cmcc.vrp.boss.guangdongpool.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.LinkedList;

/**
 * 成员订购产品信息
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
@XStreamAlias("Product")
public class Product {
    @XStreamAlias("OptType")
    private String optType;

    @XStreamAlias("MemberNum")
    private String memberNum;

    @XStreamAlias("ProductCode")
    private String productCode;

    @XStreamAlias("PayFlag")
    private String payFlag;

    @XStreamAlias("EffType")
    private String effType;

    @XStreamAlias("UseCycle")
    private String useCycle;

    @XStreamAlias("PackProdid")
    private String packProdId;

    @XStreamAlias("Services")
    private LinkedList<Service> services;

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(String memberNum) {
        this.memberNum = memberNum;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(String payFlag) {
        this.payFlag = payFlag;
    }

    public String getEffType() {
        return effType;
    }

    public void setEffType(String effType) {
        this.effType = effType;
    }

    public String getUseCycle() {
        return useCycle;
    }

    public void setUseCycle(String useCycle) {
        this.useCycle = useCycle;
    }

    public String getPackProdId() {
        return packProdId;
    }

    public void setPackProdId(String packProdId) {
        this.packProdId = packProdId;
    }

    public LinkedList<Service> getServices() {
        return services;
    }

    public void setServices(LinkedList<Service> services) {
        this.services = services;
    }
}
